package com.oheat.food.service;

import com.oheat.food.dto.CategorySaveRequest;
import com.oheat.food.dto.CategoryUpdateRequest;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.exception.CategoryNotExistsException;
import com.oheat.food.exception.DuplicateCategoryException;
import com.oheat.food.repository.CategoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public void registerCategory(CategorySaveRequest saveRequest) {
        categoryRepository.findByName(saveRequest.getName())
            .ifPresent((category) -> {
                throw new DuplicateCategoryException();
            });

        categoryRepository.save(saveRequest.toEntity());
    }

    public List<CategoryJpaEntity> findAllCategory() {
        return categoryRepository.findAll();
    }

    @Transactional
    public void updateCategory(CategoryUpdateRequest updateRequest) {
        CategoryJpaEntity category = categoryRepository.findByName(updateRequest.getPrevName())
            .orElseThrow(CategoryNotExistsException::new);

        category.changeName(updateRequest.getNewName());
    }

    @Transactional
    public void deleteCategory(String categoryName) {
        CategoryJpaEntity category = categoryRepository.findByName(categoryName)
            .orElseThrow(CategoryNotExistsException::new);

        categoryRepository.deleteByName(categoryName);
    }
}
