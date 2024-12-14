package com.oheat.shop.repository;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import java.util.List;
import java.util.Optional;

public interface ShopRepository {

    void save(ShopJpaEntity shopJpaEntity);

    Optional<ShopJpaEntity> findById(Long id);

    Optional<ShopJpaEntity> findByName(String name);

    List<ShopJpaEntity> findByCategory(CategoryJpaEntity category);

    void deleteByName(String name);
}
