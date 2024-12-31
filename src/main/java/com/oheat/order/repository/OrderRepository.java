package com.oheat.order.repository;

import com.oheat.order.entity.Order;
import com.oheat.user.entity.UserJpaEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    void save(Order order);

    Optional<Order> findById(Long orderId);

    void delete(Order order);

    Page<Order> findByUser(UserJpaEntity user, Pageable pageable);
}
