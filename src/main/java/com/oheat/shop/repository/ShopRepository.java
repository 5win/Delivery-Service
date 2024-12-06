package com.oheat.shop.repository;

import com.oheat.shop.entity.ShopJpaEntity;
import java.util.Optional;

public interface ShopRepository {

    void save(ShopJpaEntity shopJpaEntity);

    Optional<ShopJpaEntity> findByName(String name);
}
