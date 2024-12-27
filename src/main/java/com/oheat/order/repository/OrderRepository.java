package com.oheat.order.repository;

import com.oheat.order.entity.Order;
import java.util.Optional;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(Long orderId);
}
