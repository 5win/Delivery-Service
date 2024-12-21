package com.oheat.order.repository;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CartRepositoryImpl implements CartRepository {

    private final CartJpaRepository cartJpaRepository;

    @Override
    public void save(CartJpaEntity cart) {
        cartJpaRepository.save(cart);
    }

    @Override
    public List<CartJpaEntity> findAllByUserAndMenu(UserJpaEntity user, MenuJpaEntity menu) {
        return cartJpaRepository.findAllByUserAndMenu(user, menu);
    }
}