package com.oheat.order;

import com.oheat.order.entity.Order;
import com.oheat.order.repository.OrderRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> orders = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(Order order) {
        orders.put(autoId++, order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public void delete(Order order) {
        Long target = orders.entrySet().stream()
            .filter(e -> e.getValue().equals(order))
            .findFirst().get()
            .getKey();
        orders.remove(target);
    }
}
