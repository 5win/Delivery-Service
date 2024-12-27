package com.oheat.order.service;

import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.order.dto.OrderSaveRequest;
import com.oheat.order.entity.Order;
import com.oheat.order.repository.OrderRepository;
import com.oheat.user.entity.CartJpaEntity;
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

        ShopJpaEntity shop = carts.getFirst()
            .getShop();
        Order order = saveRequest.toEntity(shop, user);
        orderRepository.save(order);
    }
}
