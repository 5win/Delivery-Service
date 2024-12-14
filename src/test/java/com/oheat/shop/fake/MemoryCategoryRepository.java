package com.oheat.shop.fake;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.repository.CategoryRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryCategoryRepository implements CategoryRepository {

    private final Map<String, CategoryJpaEntity> categories = new HashMap<>();

    @Override
    public void save(CategoryJpaEntity categoryJpaEntity) {
        categories.put(categoryJpaEntity.getName(), categoryJpaEntity);
    }

    @Override
    public Optional<CategoryJpaEntity> findByName(String name) {
        return Optional.ofNullable(categories.get(name));
    }

    @Override
    public List<CategoryJpaEntity> findAll() {
        return categories.values().stream().toList();
    }

    @Override
    public void deleteByName(String categoryName) {
        categories.remove(categoryName);
    }
}
