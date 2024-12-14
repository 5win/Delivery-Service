package com.oheat.food.repository;

import com.oheat.food.entity.CategoryJpaEntity;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    void save(CategoryJpaEntity categoryJpaEntity);

    Optional<CategoryJpaEntity> findByName(String name);

    List<CategoryJpaEntity> findAll();

    void delete(CategoryJpaEntity category);
}
