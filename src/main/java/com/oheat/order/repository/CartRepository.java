package com.oheat.order.repository;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import java.util.Optional;

public interface CartRepository {

    void save(CartJpaEntity cart);

    List<CartJpaEntity> findAllByUserAndMenu(UserJpaEntity user, MenuJpaEntity menu);

    Optional<CartJpaEntity> findById(Long cartId);

    void delete(CartJpaEntity cart);
}
