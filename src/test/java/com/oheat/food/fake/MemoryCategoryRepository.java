package com.oheat.food.fake;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.repository.CategoryRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
    public void delete(CategoryJpaEntity category) {
        Entry<String, CategoryJpaEntity> target = categories.entrySet()
            .stream()
            .filter(entry -> entry.getValue().getName().equals(category.getName()))
            .findFirst()
            .get();

        categories.remove(target.getKey());
    }
}
