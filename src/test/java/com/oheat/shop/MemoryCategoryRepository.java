package com.oheat.shop;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.repository.CategoryRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryCategoryRepository implements CategoryRepository {

    private final Map<String, CategoryJpaEntity> categories = new HashMap<>();

    @Override
    public void save(CategoryJpaEntity categoryJpaEntity) {
        categories.put(categoryJpaEntity.getCategory(), categoryJpaEntity);
    }

    @Override
    public Optional<CategoryJpaEntity> findByName(String name) {
        return Optional.ofNullable(categories.get(name));
    }
}
