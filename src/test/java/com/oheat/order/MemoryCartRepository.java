package com.oheat.order;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.order.repository.CartRepository;
import com.oheat.user.entity.UserJpaEntity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryCartRepository implements CartRepository {

    private final Map<Long, CartJpaEntity> carts = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(CartJpaEntity cart) {
        carts.put(autoId++, cart);
    }

    @Override
    public List<CartJpaEntity> findAllByUserAndMenu(UserJpaEntity user, MenuJpaEntity menu) {
        return carts.values().stream()
            .filter(v -> {
                return v.getUser().equals(user) && v.getMenu().equals(menu);
            })
            .toList();
    }
}
