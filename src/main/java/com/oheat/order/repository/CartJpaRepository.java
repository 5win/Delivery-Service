package com.oheat.order.repository;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<CartJpaEntity, Long> {

    List<CartJpaEntity> findAllByUserAndMenu(UserJpaEntity user, MenuJpaEntity menu);
}
