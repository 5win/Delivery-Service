package com.oheat.order.repository;

import com.oheat.order.entity.Order;
import com.oheat.user.entity.UserJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(UUID orderId);

    void delete(Order order);

    Page<Order> findByUser(UserJpaEntity user, Pageable pageable);
}
