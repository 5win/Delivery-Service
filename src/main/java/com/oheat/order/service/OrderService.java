package com.oheat.order.service;

import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.dto.OrderSaveRequest;
import com.oheat.order.entity.Order;
import com.oheat.order.entity.OrderMenu;
import com.oheat.order.entity.OrderOption;
import com.oheat.order.entity.OrderOptionGroup;
import com.oheat.order.repository.OrderRepository;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.CartOptionGroup;
import com.oheat.user.entity.CartOptionGroupOption;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.CartEmptyException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public void registerOrder(OrderSaveRequest saveRequest, String username) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);

        List<CartJpaEntity> carts = user.getCarts();
        if (carts.isEmpty()) {
            throw new CartEmptyException();
        }

        ShopJpaEntity shop = carts.getFirst().getShop();
        Order order = generateOrder(shop, user, saveRequest);

        orderRepository.save(order);
    }

    /**
     * 주문 객체 생성 메서드
     */
    private Order generateOrder(ShopJpaEntity shop, UserJpaEntity user,
        OrderSaveRequest saveRequest) {

        Order order = saveRequest.toEntity(shop, user);
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
