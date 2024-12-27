package com.oheat.order.repository;

import com.oheat.order.entity.Order;
import com.oheat.user.entity.UserJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCustomRepository {

    Page<Order> findByUser(UserJpaEntity user, Pageable pageable);
}
