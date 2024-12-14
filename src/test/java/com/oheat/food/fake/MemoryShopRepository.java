package com.oheat.food.fake;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.repository.ShopRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import lombok.Getter;

@Getter
public class MemoryShopRepository implements ShopRepository {

    private final Map<Long, ShopJpaEntity> shops = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(ShopJpaEntity shopJpaEntity) {
        shops.put(autoId++, shopJpaEntity);
    }

    @Override
    public Optional<ShopJpaEntity> findById(Long id) {
        return Optional.ofNullable(shops.get(id));
    }

    @Override
    public Optional<ShopJpaEntity> findByName(String name) {
        return shops.values().stream()
            .filter(shop -> shop.getName().equals(name))
            .findFirst();
    }

    @Override
    public List<ShopJpaEntity> findByCategory(CategoryJpaEntity category) {
        return shops.values().stream()
            .filter(shop -> shop.getCategory().equals(category))
            .toList();
    }

    @Override
    public void deleteByName(String name) {
        Entry<Long, ShopJpaEntity> target = shops.entrySet().stream()
            .filter(entry -> entry.getValue().getName().equals(name))
            .findFirst().get();

        shops.remove(target.getKey());
    }
}
