package com.oheat.order;

import com.oheat.order.entity.Order;
import com.oheat.order.repository.OrderRepository;
import com.oheat.user.entity.UserJpaEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class MemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> orders = new HashMap<>();

    @Override
    public void save(Order order) {
        orders.put(order.getId(), order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
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
        UUID target = orders.entrySet().stream()
            .filter(e -> e.getValue().equals(order))
            .findFirst().get()
            .getKey();
        orders.remove(target);
    }
}
