package com.oheat.order;

import com.oheat.order.entity.Order;
import com.oheat.order.repository.OrderRepository;
import com.oheat.user.entity.UserJpaEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public Page<Order> findByUser(UserJpaEntity user, Pageable pageable) {
        List<Order> content = orders.values().stream()
            .filter(order -> order.getUser().equals(user))
            .toList();
        return new PageImpl<>(content, PageRequest.ofSize(10), orders.size());
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
