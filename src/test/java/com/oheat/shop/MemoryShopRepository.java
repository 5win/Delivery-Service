package com.oheat.shop;

import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.repository.ShopRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;

@Getter
public class MemoryShopRepository implements ShopRepository {

    private final Map<String, ShopJpaEntity> shops = new HashMap<>();

    @Override
    public void save(ShopJpaEntity shopJpaEntity) {
        shops.put(shopJpaEntity.getName(), shopJpaEntity);
    }

    @Override
    public Optional<ShopJpaEntity> findByName(String name) {
        return Optional.ofNullable(shops.get(name));
    }
}
