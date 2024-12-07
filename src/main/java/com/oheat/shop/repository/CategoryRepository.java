package com.oheat.shop.repository;

import com.oheat.shop.entity.CategoryJpaEntity;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    void save(CategoryJpaEntity categoryJpaEntity);

    Optional<CategoryJpaEntity> findByName(String name);

    List<CategoryJpaEntity> findAll();
}
