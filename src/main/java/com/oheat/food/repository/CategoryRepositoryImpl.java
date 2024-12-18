package com.oheat.food.repository;

import com.oheat.food.entity.CategoryJpaEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class CategoryRepositoryImpl implements CategoryRepository {

    private final CategoryJpaRepository categoryJpaRepository;

    @Override
    public void save(CategoryJpaEntity categoryJpaEntity) {
        categoryJpaRepository.save(categoryJpaEntity);
    }

    @Override
    public Optional<CategoryJpaEntity> findByName(String name) {
        return categoryJpaRepository.findByName(name);
    }

    @Override
    public List<CategoryJpaEntity> findAll() {
        return categoryJpaRepository.findAll();
    }

    @Override
    public void delete(CategoryJpaEntity category) {
        categoryJpaRepository.delete(category);
    }
}
