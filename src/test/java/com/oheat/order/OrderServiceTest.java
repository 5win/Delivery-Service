package com.oheat.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.constant.OrderState;
import com.oheat.order.constant.PayMethod;
import com.oheat.order.constant.PaymentState;
import com.oheat.order.dto.OrderSaveRequest;
import com.oheat.order.entity.Order;
import com.oheat.order.entity.OrderMenu;
import com.oheat.order.entity.OrderOption;
import com.oheat.order.entity.OrderOptionGroup;
import com.oheat.order.entity.Payment;
import com.oheat.order.exception.InvalidPaymentInfoException;
import com.oheat.order.exception.OrderNotExistsException;
import com.oheat.order.exception.PaymentNotConfirmedException;
import com.oheat.order.exception.PaymentNotExistsException;
import com.oheat.order.repository.OrderRepository;
import com.oheat.order.repository.PaymentRepository;
import com.oheat.order.service.OrderService;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.CartOptionGroup;
import com.oheat.user.entity.CartOptionGroupOption;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.CartEmptyException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.repository.UserRepositoryImpl;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;

public class OrderServiceTest {

    // mockito stub
    private final UserRepository userRepository = Mockito.mock(UserRepositoryImpl.class);

    // fake DB
    private final OrderRepository memoryOrderRepository = new MemoryOrderRepository();
    private final PaymentRepository memoryPaymentRepository = new MemoryPaymentRepository();

    // service
    private OrderService orderService;
    private final ApplicationEventPublisher applicationEventPublisher = Mockito.mock(ApplicationEventPublisher.class);

    @BeforeEach
    void setUp() {
        orderService = new OrderService(userRepository, memoryOrderRepository, memoryPaymentRepository,
            applicationEventPublisher);
    }

    // Create
    @Test
    @DisplayName("주문 시, 잘못된 사용자면 UserNotExistsException")
    void givenWrongUser_whenOrder_thenThrowUserNotExistsException() {
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.empty());

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();

        Assertions.assertThrows(UserNotExistsException.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("장바구니가 비어있다면, CartEmptyException")
    void whenCartIsEmpty_thenThrowCartEmptyException() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("user")
            .build();

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey("tgen_20250102210202h9Oy0")
            .build();

        Assertions.assertThrows(CartEmptyException.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("paymentKey가 Payment 테이블에 존재하지 않으면, PaymentNotExistsException")
    void whenPaymentKeyNotExistsInPaymentTable_thenThrowPaymentNotExistsException() {
        UserJpaEntity user = generateUserWithCarts();

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey("tgen_20250102210202h9Oy0")
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        Assertions.assertThrows(PaymentNotExistsException.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("전달된 결제 정보가 승인되지 않은 결제이면, PaymentNotConfirmedException")
    void whenPaymentNotConfirmed_thenThrowPaymentNotConfirmedException() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 30_500, PaymentState.UNCONFIRMED); // 승인되지 않은 결제

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey("tgen_20250102210202h9Oy0")
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        Assertions.assertThrows(PaymentNotConfirmedException.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("전달된 결제 정보가 취소된 결제이면, PaymentNotConfirmedException")
    void whenPaymentCancelled_thenThrowPaymentNotConfirmedException() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 30_500, PaymentState.CANCELLED); // 취소된 결제

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey("tgen_20250102210202h9Oy0")
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        Assertions.assertThrows(PaymentNotConfirmedException.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("결제 정보의 amount값이 장바구니의 totalPayAmount값과 일치하지 않으면, InvalidPaymentInfoException")
    void whenAmountNotEqualsTotalPayAmount_thenThrowInvalidPaymentInfoException() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 30_500, PaymentState.CONFIRMED);

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        // totalPayAmount = 28,000 + 3,000
        Assertions.assertThrows(InvalidPaymentInfoException.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("주문 시, 주문 테이블에 주문 상세 정보가 저장된다.")
    void whenOrder_thenSaveToOrdersTable() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.of(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        Assertions.assertDoesNotThrow(() -> {
            orderService.registerOrder(saveReq, "user");
        });

        Order result = memoryOrderRepository.findById(1L).get();
        assertThat(result.getAddress()).isEqualTo("서울특별시");
        assertThat(result.getPhone()).isEqualTo("010-1234-1234");
        assertThat(result.getDeliveryFee()).isEqualTo(3000);
        assertThat(result.getPayMethod()).isEqualTo(PayMethod.TOSS);
    }

    @Test
    @DisplayName("주문 직후, 주문 상태는 pending 상태이다.")
    void whenOrderSuccess_thenOrderStateIsPending() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.of(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        orderService.registerOrder(saveReq, "user");
        Order result = memoryOrderRepository.findById(1L).get();

        assertThat(result.getOrderState()).isEqualTo(OrderState.PENDING);
    }

    @Test
    @DisplayName("장바구니에 담긴 메뉴를 주문 메뉴 테이블에 저장한다.")
    void givenCarts_whenOrder_thenSaveMenuToOrdersMenuTable() {
        // 매뉴를 유저의 장바구니에 추가
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        orderService.registerOrder(saveReq, "user");

        // 결과
        Order orderResult = memoryOrderRepository.findById(1L).get();
        MenuJpaEntity menuResult = orderResult.getOrderMenus().getFirst()
            .getMenu();

        assertThat(orderResult.getOrderMenus().size()).isEqualTo(1);
        assertThat(menuResult.getName()).isEqualTo("황올");
    }

    @Test
    @DisplayName("장바구니에 담긴 메뉴의 옵션 그룹을 주문 옵션 그룹 테이블에 저장한다.")
    void givenCarts_whenOrder_thenSaveOptionGroupToOrdersOptionGroupTable() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        orderService.registerOrder(saveReq, "user");

        // 결과
        Order orderResult = memoryOrderRepository.findById(1L).get();
        List<OrderMenu> orderMenuResult = orderResult.getOrderMenus();
        List<OrderOptionGroup> orderOptionGroupResult = orderMenuResult.getFirst()
            .getOrderOptionGroups();

        assertThat(orderOptionGroupResult.size()).isEqualTo(2);
        assertThat(orderOptionGroupResult.get(0).getOptionGroup().getName()).isEqualTo("부분육 선택");
        assertThat(orderOptionGroupResult.get(1).getOptionGroup().getName()).isEqualTo("음료 선택");
    }

    @Test
    @DisplayName("장바구니에 담긴 메뉴의 옵션을 주문 옵션 테이블에 저장한다.")
    void givenCarts_whenOrder_thenSaveOptionToOrderOptionTable() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        // 주문
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        orderService.registerOrder(saveReq, "user");

        // 결과
        Order order = memoryOrderRepository.findById(1L).get();
        OrderMenu orderMenu = order.getOrderMenus().getFirst();
        OrderOptionGroup orderOptionGroups1 = orderMenu.getOrderOptionGroups().get(0);
        OrderOptionGroup orderOptionGroups2 = orderMenu.getOrderOptionGroups().get(1);
        List<OrderOption> orderOptions1 = orderOptionGroups1.getOrderOptions();
        List<OrderOption> orderOptions2 = orderOptionGroups2.getOrderOptions();

        assertThat(orderOptions1.size()).isEqualTo(1);
        assertThat(orderOptions2.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("결제 승인에 성공하고 주문을 저장한 뒤, 장바구니를 비우는 과정에서 문제 발생 시 롤백하고 500을 반환한다.")
    void afterOrder_whenErrorAtClearingCart_thenRollbackAndReturn500() {
        UserJpaEntity spyUser = Mockito.spy(generateUserWithCarts());

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        // 주문
        when(userRepository.findByUsername("user"))
            .thenReturn(Optional.ofNullable(spyUser));

        doThrow(RuntimeException.class)
            .when(spyUser)
            .clearCart();

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        Assertions.assertThrows(Exception.class, () -> {
            orderService.registerOrder(saveReq, "user");
        });
    }

    @Test
    @DisplayName("결제 승인에 성공하고 주문을 저장했으면, 장바구니를 비운다.")
    void whenSuccessOrder_thenCartIsEmpty() {
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 31_000, PaymentState.CONFIRMED);

        // 주문
        when(userRepository.findByUsername("user"))
            .thenReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        Assertions.assertDoesNotThrow(() -> {
            orderService.registerOrder(saveReq, "user");
        });
        assertThat(user.getCarts()).hasSize(0);
    }

    // Read
    @Test
    @DisplayName("주문 내역 조회 시, 각 주문의 매장 이름, 결제 금액, 주문 상태, 리뷰 여부 정보를 Page로 반환한다.")
    void whenFindOrderHistory_thenReturnInfoByPage() {
        // 주문 등록
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 30_500, PaymentState.CONFIRMED);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(500)
            .payMethod(PayMethod.TOSS)
            .build();
        orderService.registerOrder(saveReq, "user");

        // 주문 조회
        Order result = orderService.findOrderByUser("user", PageRequest.of(0, 10))
            .getContent().get(0);

        assertThat(result.getShop().getName()).isEqualTo("bbq");
        assertThat(result.calcPayAmount()).isEqualTo(20_000 + 4_000 + 2_000 + 2_000 + 3_000 - 500); // 30,500
        assertThat(result.getOrderState()).isEqualTo(OrderState.PENDING);
        assertThat(result.isReviewed()).isFalse();
    }

    @Test
    @DisplayName("결제할 금액이 음수이면, 0원으로 조회됨")
    void whenPayAmountIsNegative_thenSetZero() {
        // 주문 등록
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 0, PaymentState.CONFIRMED);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(999_999_999)
            .payMethod(PayMethod.TOSS)
            .build();
        orderService.registerOrder(saveReq, "user");

        // 주문 조회
        Order result = orderService.findOrderByUser("user", PageRequest.of(0, 10))
            .getContent().get(0);

        assertThat(result.calcPayAmount()).isEqualTo(0);
    }

    @Test
    @DisplayName("주문 상세 조회 시, 주문 일시, 주소, 전화번호, 결제 방법, 메뉴, 옵션 정보를 반환한다.")
    void whenFindOrderDetail_thenReturnDetailInfo() {
        // 주문 등록
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 30_500, PaymentState.CONFIRMED);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(500)
            .payMethod(PayMethod.TOSS)
            .build();
        orderService.registerOrder(saveReq, "user");

        // 주문 상세 조회
        Order order = orderService.findOrderById(1L);
        List<OrderMenu> orderMenus = order.getOrderMenus();
        List<OrderOptionGroup> orderOptionGroups = orderMenus.get(0).getOrderOptionGroups();
        List<OrderOption> orderOptions = orderOptionGroups.get(0).getOrderOptions();

        assertThat(order.getAddress()).isEqualTo("서울특별시");
        assertThat(order.getPhone()).isEqualTo("010-1234-1234");
        assertThat(order.getPayMethod()).isEqualTo(PayMethod.TOSS);
        assertThat(orderMenus.size()).isEqualTo(1);
        assertThat(orderOptionGroups.size()).isEqualTo(2);
        assertThat(orderOptions.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("주문 상세 조회 시, 메뉴 금액, 배달팁, 할인 금액, 총 금액, 결제할 금액을 반환한다.")
    void whenFindOrderDetail_thenReturnMenuPriceAndDeliveryFeeAndDiscountAndTotalPriceAndPayAmount() {
        // 주문 등록
        UserJpaEntity user = generateUserWithCarts();

        // 결제
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(UUID.randomUUID(), paymentKey, 30_500, PaymentState.CONFIRMED);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(500)
            .payMethod(PayMethod.TOSS)
            .build();
        orderService.registerOrder(saveReq, "user");

        // 주문 상세 조회
        Order order = orderService.findOrderById(1L);
        OrderMenu orderMenu = order.getOrderMenus().get(0);

        assertThat(orderMenu.calcTotalPrice()).isEqualTo(20_000 + 4_000 + 2_000 + 2_000);
        assertThat(order.getDeliveryFee()).isEqualTo(3_000);
        assertThat(order.getDiscount()).isEqualTo(500);
        assertThat(order.calcPayAmount()).isEqualTo(28_000 + 3_000 - 500);
    }

    // Delete
    @Test
    @DisplayName("주문이 존재하지 않으면, OrderNotExistsException")
    void givenWrongOrderId_whenDeleteOrderHistory_thenThrowOrderNotExistsException() {
        Assertions.assertThrows(OrderNotExistsException.class, () -> {
            orderService.deleteOrderHistoryById(1L);
        });
    }

    @Test
    @DisplayName("주문 id로 주문 내역을 삭제한다.")
    void givenOrder_whenDeleteOrderHistory_thenDoesNotThrow() {
        memoryOrderRepository.save(Order.builder().build());

        Assertions.assertDoesNotThrow(() -> {
            orderService.deleteOrderHistoryById(1L);
        });
    }

    private void savePayment(UUID orderId, String paymentKey, int amount, PaymentState state) {
        memoryPaymentRepository.save(Payment.builder()
            .orderId(orderId)
            .paymentKey(paymentKey)
            .totalAmount(amount)
            .state(state)
            .build());
    }

    private UserJpaEntity generateUserWithCarts() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("user").address("서울특별시").phone("010-1234-1234").build();
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder()
            .name("황올").price(20_000).shop(shop).build();
        OptionGroupJpaEntity optionGroup1 = OptionGroupJpaEntity.builder()
            .name("부분육 선택").menu(menu).build();
        OptionGroupJpaEntity optionGroup2 = OptionGroupJpaEntity.builder()
            .name("음료 선택").menu(menu).build();
        OptionJpaEntity option1 = OptionJpaEntity.builder()
            .name("순살").price(4_000).optionGroup(optionGroup1).build();
        OptionJpaEntity option2 = OptionJpaEntity.builder()
            .name("콜라").price(2_000).optionGroup(optionGroup2).build();
        OptionJpaEntity option3 = OptionJpaEntity.builder()
            .name("사이다").price(2_000).optionGroup(optionGroup2).build();

        CartJpaEntity cart = CartJpaEntity.builder()
            .amount(1).user(user).shop(shop).menu(menu).build();
        CartOptionGroup cartOptionGroup1 = CartOptionGroup.builder()
            .cart(cart).optionGroup(optionGroup1).build();
        CartOptionGroup cartOptionGroup2 = CartOptionGroup.builder()
            .cart(cart).optionGroup(optionGroup2).build();
        CartOptionGroupOption cartOptionGroupOption1 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup1).option(option1).build();
        CartOptionGroupOption cartOptionGroupOption2 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup2).option(option2).build();
        CartOptionGroupOption cartOptionGroupOption3 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup2).option(option3).build();

        cartOptionGroup1.addCartOption(cartOptionGroupOption1);
        cartOptionGroup2.addCartOption(cartOptionGroupOption2);
        cartOptionGroup2.addCartOption(cartOptionGroupOption3);
        cart.addCartOptionGroup(cartOptionGroup1);
        cart.addCartOptionGroup(cartOptionGroup2);
        user.addToCart(cart);

        return user;
    }
}
