package com.oheat.user;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.repository.CartRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @Override
    public Optional<CartJpaEntity> findById(Long cartId) {
        return Optional.ofNullable(carts.get(cartId));
    }

    @Override
    public void delete(CartJpaEntity cart) {
        Long target = carts.entrySet().stream()
            .filter(entry -> entry.getValue().equals(cart))
            .findFirst()
            .get().getKey();
        carts.remove(target);
    }
}
