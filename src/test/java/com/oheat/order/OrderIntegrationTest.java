package com.oheat.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.repository.CategoryJpaRepository;
import com.oheat.food.repository.MenuJpaRepository;
import com.oheat.food.repository.OptionGroupJpaRepository;
import com.oheat.food.repository.OptionJpaRepository;
import com.oheat.food.repository.ShopJpaRepository;
import com.oheat.order.constant.PayMethod;
import com.oheat.order.dto.OrderSaveRequest;
import com.oheat.order.entity.Order;
import com.oheat.order.entity.Payment;
import com.oheat.order.repository.OrderJpaRepository;
import com.oheat.order.repository.PaymentJpaRepository;
import com.oheat.order.service.OrderService;
import com.oheat.user.constant.Role;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.CartOptionGroup;
import com.oheat.user.entity.CartOptionGroupOption;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.repository.CartJpaRepository;
import com.oheat.user.repository.UserJpaRepository;
import jakarta.persistence.EntityManager;
import java.sql.Connection;
import java.util.Optional;
import java.util.UUID;
import org.hibernate.Session;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class OrderIntegrationTest {

    // Mockito Bean
    @MockitoSpyBean
    private UserJpaRepository userJpaRepository;
    @MockitoSpyBean
    private OrderJpaRepository orderJpaRepository;

    // Repository Bean
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private ShopJpaRepository shopJpaRepository;
    @Autowired
    private MenuJpaRepository menuJpaRepository;
    @Autowired
    private OptionGroupJpaRepository optionGroupJpaRepository;
    @Autowired
    private OptionJpaRepository optionJpaRepository;
    @Autowired
    private CartJpaRepository cartJpaRepository;
    @Autowired
    private PaymentJpaRepository paymentJpaRepository;

    // Service Bean
    @Autowired
    private OrderService orderService;

    @Autowired
    private EntityManager entityManager;

    @BeforeEach
    void reset() {
        entityManager.createNativeQuery("ALTER TABLE users AUTO_INCREMENT = 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE shop AUTO_INCREMENT = 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE menu AUTO_INCREMENT = 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE option_group AUTO_INCREMENT = 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE options AUTO_INCREMENT = 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE cart AUTO_INCREMENT = 1")
            .executeUpdate();
    }

    @Test
    @DisplayName("장바구니를 비우는 과정에서 문제 발생 시, 저장된 주문 데이터를 롤백한다.")
    void whenClearCartFail_thenRollbackOrder() {
        // 유저, 매장 등등 정보 생성 후 장바구니에 담음
        addToCart();

        // 승인된 결제 정보 저장
        UUID orderId = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(orderId, paymentKey, 26_000, true);

        // User, UserRepository 모킹
        UserJpaEntity spyUser = Mockito.spy(userJpaRepository.findByUsername("username").get());
        doThrow(RuntimeException.class)
            .when(spyUser)
            .clearCart();

        when(userJpaRepository.findByUsername("username"))
            .thenReturn(Optional.of(spyUser));

        // 주문
        OrderSaveRequest orderSaveRequest = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .deliveryFee(0)
            .payMethod(PayMethod.TOSS)
            .discount(0)
            .build();

        entityManager.flush();

        Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.registerOrder(orderSaveRequest, "username");
        });
        Optional<Order> result = orderJpaRepository.findById(1L);
        assertThat(result).isNotPresent();
    }

    @Test
    @DisplayName("주문 데이터를 저장하는 과정(save)에서 문제 발생 시, 결제를 취소한다.")
    void whenSaveOrderFail_thenRollbackOrder() {
        // 유저, 매장 등등 정보 생성 후 장바구니에 담음
        addToCart();

        // 승인된 결제 정보 저장
        UUID orderId = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(orderId, paymentKey, 26_000, true);

        // OrderRepository 모킹
        doThrow(RuntimeException.class)
            .when(orderJpaRepository)
            .save(any());

        // 주문
        OrderSaveRequest orderSaveRequest = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .deliveryFee(0)
            .payMethod(PayMethod.TOSS)
            .discount(0)
            .build();

        Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.registerOrder(orderSaveRequest, "username");
        });
        Optional<Order> result = orderJpaRepository.findById(1L);
        assertThat(result).isNotPresent();
    }

    @Disabled
    @Test
    @DisplayName("장바구니를 비우는 과정에서 문제 발생 시, 결제를 취소한다.")
    void test3() {

    }

    @Test
    @DisplayName("결제부터 주문 성공까지의 프로세스를 성공한다.")
    void successPaymentAndOrder() {
        // 유저, 매장 등등 정보 생성 후 장바구니에 담음
        addToCart();

        // 승인된 결제 정보 저장
        UUID orderId = UUID.randomUUID();
        String paymentKey = "tgen_20250102210202h9Oy0";
        savePayment(orderId, paymentKey, 26_000, true);

        // 주문
        OrderSaveRequest orderSaveRequest = OrderSaveRequest.builder()
            .paymentKey(paymentKey)
            .deliveryFee(0)
            .payMethod(PayMethod.TOSS)
            .discount(0)
            .build();

        entityManager.flush();
        entityManager.clear();

        Session session = entityManager.unwrap(Session.class);
        System.out.println("Transaction Name: " + TransactionSynchronizationManager.getCurrentTransactionName());
        System.out.println("Isolation Level: " + session.doReturningWork(Connection::getTransactionIsolation));

        Assertions.assertDoesNotThrow(() -> {
            orderService.registerOrder(orderSaveRequest, "username");
        });
    }

    Payment savePayment(UUID orderId, String paymentKey, int amount, boolean paid) {
        Payment payment = Payment.builder()
            .orderId(orderId)
            .paymentKey(paymentKey)
            .amount(amount)
            .paid(paid)
            .build();
        paymentJpaRepository.save(payment);
        return payment;
    }

    void addToCart() {
        // 유저, 매장, 메뉴 등등의 정보를 미리 저장
        UserJpaEntity user = UserJpaEntity.builder()
            .username("username")
            .password("pw")
            .address("서울특별시")
            .phone("010-1234-1234")
            .role(Role.CUSTOMER)
            .build();
        CategoryJpaEntity category = CategoryJpaEntity.builder()
            .name("치킨")
            .build();
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .name("bbq")
            .minimumOrderAmount(10000)
            .deliveryFee(2000)
            .category(category)
            .build();
        MenuJpaEntity menu = MenuJpaEntity.builder()
            .name("황올")
            .price(20000)
            .shop(shop)
            .build();
        OptionGroupJpaEntity optionGroup1 = OptionGroupJpaEntity.builder()
            .name("부분육 선택")
            .menu(menu)
            .build();
        OptionGroupJpaEntity optionGroup2 = OptionGroupJpaEntity.builder()
            .name("음료 선택")
            .menu(menu)
            .build();
        OptionJpaEntity option1 = OptionJpaEntity.builder()
            .name("순살")
            .optionGroup(optionGroup1)
            .price(1000)
            .build();
        OptionJpaEntity option2 = OptionJpaEntity.builder()
            .name("콜라")
            .optionGroup(optionGroup2)
            .price(2000)
            .build();
        OptionJpaEntity option3 = OptionJpaEntity.builder()
            .name("사이다")
            .optionGroup(optionGroup2)
            .price(3000)
            .build();

        userJpaRepository.save(user);
        categoryJpaRepository.save(category);
        shopJpaRepository.save(shop);
        menuJpaRepository.save(menu);
        optionGroupJpaRepository.save(optionGroup1);
        optionGroupJpaRepository.save(optionGroup2);
        optionJpaRepository.save(option1);
        optionJpaRepository.save(option2);
        optionJpaRepository.save(option3);

        // 장바구니에 미리 아이템 추가
        // 총 옵션 그룹은 2개이며, 각 그룹에서 1개와 2개의 옵션이 선택됨

        // 옵션그룹1
        CartJpaEntity cart = CartJpaEntity.builder()
            .amount(1)
            .user(user)
            .shop(shop)
            .menu(menu)
            .build();
        CartOptionGroup cartOptionGroup1 = CartOptionGroup.builder()
            .cart(cart)
            .optionGroup(optionGroup1)
            .build();
        CartOptionGroupOption cartOptionGroupOption1 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup1)
            .option(option1)
            .build();
        cartOptionGroup1.addCartOption(cartOptionGroupOption1);
        cart.addCartOptionGroup(cartOptionGroup1);

        // 옵션그룹2
        CartOptionGroup cartOptionGroup2 = CartOptionGroup.builder()
            .cart(cart)
            .optionGroup(optionGroup2)
            .build();
        CartOptionGroupOption cartOptionGroupOption2 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup2)
            .option(option2)
            .build();
        CartOptionGroupOption cartOptionGroupOption3 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup2)
            .option(option3)
            .build();
        cartOptionGroup2.addCartOption(cartOptionGroupOption2);
        cartOptionGroup2.addCartOption(cartOptionGroupOption3);
        cart.addCartOptionGroup(cartOptionGroup2);

        // 장바구니에 담음
        cartJpaRepository.save(cart);
        user.addToCart(cart);
    }
}
