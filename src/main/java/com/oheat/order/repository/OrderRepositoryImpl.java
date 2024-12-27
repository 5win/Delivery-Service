package com.oheat.order.repository;

import com.oheat.order.entity.Order;
import com.oheat.user.entity.UserJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public void save(Order order) {
        orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(Long orderId) {
        return orderJpaRepository.findById(orderId);
    }

    @Override
    public Page<Order> findByUser(UserJpaEntity user, Pageable pageable) {
        return orderJpaRepository.findByUser(user, pageable);
    }

    @Override
    public void delete(Order order) {
        orderJpaRepository.delete(order);
    }

}
