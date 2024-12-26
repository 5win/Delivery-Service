package com.oheat.food.repository;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShopCustomRepository {

    Page<ShopJpaEntity> findShopByCategory(CategoryJpaEntity category, Pageable pageable);
}
