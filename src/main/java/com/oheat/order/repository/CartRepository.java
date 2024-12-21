package com.oheat.order.repository;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;

public interface CartRepository {

    void save(CartJpaEntity cart);

    List<CartJpaEntity> findAllByUserAndMenu(UserJpaEntity user, MenuJpaEntity menu);
}