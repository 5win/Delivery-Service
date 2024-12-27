package com.oheat.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.constant.OrderState;
import com.oheat.order.constant.PayMethod;
import com.oheat.order.dto.OrderSaveRequest;
import com.oheat.order.entity.Order;
import com.oheat.order.repository.OrderRepository;
import com.oheat.order.service.OrderService;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.CartEmptyException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.repository.UserRepositoryImpl;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderServiceTest {

    // mockito stub
    private final UserRepository userRepository = Mockito.mock(UserRepositoryImpl.class);

    // fake DB
    private final OrderRepository memoryOrderRepository = new MemoryOrderRepository();

    // service
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderService(userRepository, memoryOrderRepository);
    }

    // Create
    @Test
    @DisplayName("주문 시, 잘못된 사용자면 UserNotExistsException")
    void givenWrongUser_whenOrder_thenThrowUserNotExistsException() {
        given(userRepository.findByUsername("user"))
            .willReturn(Optional.empty());

        Assertions.assertThrows(UserNotExistsException.class, () -> {
            orderService.registerOrder(null, "user");
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

        Assertions.assertThrows(CartEmptyException.class, () -> {
            orderService.registerOrder(null, "user");
        });
    }

    @Test
    @DisplayName("주문 시, 주문 테이블에 주문 상세 정보가 저장된다.")
    void whenOrder_thenSaveToOrdersTable() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("user")
            .address("서울특별시")
            .phone("010-1234-1234")
            .build();
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .name("bbq")
            .build();
        CartJpaEntity cart = CartJpaEntity.builder()
            .shop(shop)
            .build();
        user.addToCart(cart);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
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
        UserJpaEntity user = UserJpaEntity.builder()
            .username("user")
            .build();
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .name("bbq")
            .build();
        CartJpaEntity cart = CartJpaEntity.builder()
            .shop(shop)
            .build();
        user.addToCart(cart);

        given(userRepository.findByUsername("user"))
            .willReturn(Optional.ofNullable(user));

        OrderSaveRequest saveReq = OrderSaveRequest.builder()
            .msgForShop("치킨무X")
            .deliveryFee(3000)
            .discount(0)
            .payMethod(PayMethod.TOSS)
            .build();

        orderService.registerOrder(saveReq, "user");
        Order result = memoryOrderRepository.findById(1L).get();

        assertThat(result.getOrderState()).isEqualTo(OrderState.PENDING);
    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담긴 메뉴를 주문 메뉴 테이블에 저장한다.")
    void test4() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담긴 메뉴의 옵션 그룹을 주문 옵션 그룹 테이블에 저장한다.")
    void test5() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담긴 메뉴의 옵션을 주문 옵션 테이블에 저장한다.")
    void test6() {

    }

    // Read
    @Disabled
    @Test
    @DisplayName("주문 내역 조회 시, 각 주문의 매장, 결제 금액, 주문 상태 정보를 Page로 반환한다.")
    void test7() {

    }

    @Disabled
    @Test
    @DisplayName("주문 상세 조회 시, 주문 기본 정보를 반환한다.")
    void test8() {

    }

    @Disabled
    @Test
    @DisplayName("주문 상세 조회 시, 각 주문 항목의 정보를 리스트로 반환한다.")
    void test9() {

    }

    @Disabled
    @Test
    @DisplayName("주문 상세 조회 시, 메뉴 금액, 배달팁, 할인 금액, 총 금액을 반환한다.")
    void test10() {

    }

    @Disabled
    @Test
    @DisplayName("주문 상세 조회 시, 결제 방법을 반환한다.")
    void test11() {

    }

    // Delete
    @Disabled
    @Test
    @DisplayName("주문 id로 주문 내역을 삭제한다.")
    void test12() {

    }
}
