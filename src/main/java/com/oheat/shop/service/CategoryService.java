package com.oheat.shop.service;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.exception.CategoryNotExistsException;
import com.oheat.shop.exception.DuplicateCategoryException;
import com.oheat.shop.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void registerCategory(String categoryName) {
        categoryRepository.findByName(categoryName)
            .ifPresent((category) -> {
                throw new DuplicateCategoryException();
            });

        categoryRepository.save(CategoryJpaEntity.builder()
            .name(categoryName)
            .build());
    }

    public List<String> findAllCategory() {
        return categoryRepository.findAll()
            .stream().map(CategoryJpaEntity::getName)
            .toList();
    }

    @Transactional
    public void updateCategory(String prevName, String newName) {
        CategoryJpaEntity category = categoryRepository.findByName(prevName)
            .orElseThrow(CategoryNotExistsException::new);

        category.changeName(newName);
    }

    public void deleteCategory(String categoryName) {
        CategoryJpaEntity category = categoryRepository.findByName(categoryName)
            .orElseThrow(CategoryNotExistsException::new);

        categoryRepository.deleteByName(categoryName);
    }
}
