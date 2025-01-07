package com.oheat.order.service;

import com.oheat.food.entity.ShopJpaEntity;
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
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.CartOptionGroup;
import com.oheat.user.entity.CartOptionGroupOption;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.CartEmptyException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void registerOrder(OrderSaveRequest saveRequest, String username) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);

        List<CartJpaEntity> carts = user.getCarts();
        if (carts.isEmpty()) {
            throw new CartEmptyException();
        }

        Payment payment = paymentRepository.findById(saveRequest.getPaymentKey())
            .orElseThrow(() -> new PaymentNotExistsException(HttpStatus.BAD_REQUEST, "결제 정보가 존재하지 않습니다."));
        if (!payment.isConfirmed()) {
            throw new PaymentNotConfirmedException(HttpStatus.BAD_REQUEST, "승인되지 않은 결제입니다.");
        }

        ShopJpaEntity shop = carts.getFirst().getShop();
        Order order = generateOrder(shop, user, payment, saveRequest);

        if (!order.validatePayAmount()) {
            throw new InvalidPaymentInfoException(HttpStatus.BAD_REQUEST, "결제 정보가 일치하지 않습니다.");
        }

        orderRepository.save(order);
        user.clearCart();
    }

    public Page<Order> findOrderByUser(String username, Pageable pageable) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);
        return orderRepository.findByUser(user, pageable);
    }

    public Order findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
            .orElseThrow(OrderNotExistsException::new);
    }

    public void deleteOrderHistoryById(Long orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(OrderNotExistsException::new);
        orderRepository.delete(order);
    }

    /**
     * 주문 객체 생성 메서드
     */
    private Order generateOrder(ShopJpaEntity shop, UserJpaEntity user, Payment payment, OrderSaveRequest saveRequest) {
        Order order = saveRequest.toEntity(shop, user, payment);
        user.getCarts().forEach(cart -> {
            order.addOrderMenu(generateOrderMenu(cart));
        });
        return order;
    }

    /**
     * 주문한 각 메뉴 객체들 생성 메서드
     */
    private OrderMenu generateOrderMenu(CartJpaEntity cart) {
        OrderMenu orderMenu = OrderMenu.builder()
            .amount(cart.getAmount())
            .menu(cart.getMenu())
            .build();

        cart.getCartOptionGroups().forEach(cartOptionGroup -> {
            orderMenu.addOrderOptionGroup(generateOrderOptionGroup(cartOptionGroup));
        });

        return orderMenu;
    }

    /**
     * 각 메뉴에 속한 옵션 그룹 객체들 생성 메서드
     */
    private OrderOptionGroup generateOrderOptionGroup(CartOptionGroup cartOptionGroup) {
        OrderOptionGroup orderOptionGroup = OrderOptionGroup.builder()
            .optionGroup(cartOptionGroup.getOptionGroup())
            .build();

        cartOptionGroup.getCartOptionGroupOptions().forEach(cartOptionGroupOption -> {
            orderOptionGroup.addOrderOption(generateOrderOption(cartOptionGroupOption));
        });

        return orderOptionGroup;
    }

    /**
     * 각 옵션 그룹의 옵션 객체들 생성 메서드
     */
    private OrderOption generateOrderOption(CartOptionGroupOption cartOptionGroupOption) {
        return OrderOption.builder()
            .option(cartOptionGroupOption.getOption())
            .build();
    }
}
