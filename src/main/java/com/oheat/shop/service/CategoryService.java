package com.oheat.shop.service;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.exception.DuplicateCategoryException;
import com.oheat.shop.repository.CategoryRepository;
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
}
