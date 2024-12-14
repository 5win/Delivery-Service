package com.oheat.food.serviceTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.exception.CategoryNotExistsException;
import com.oheat.food.exception.DuplicateCategoryException;
import com.oheat.food.fake.MemoryCategoryRepository;
import com.oheat.food.repository.CategoryRepository;
import com.oheat.food.service.CategoryService;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CategoryCRUDTest {

    private CategoryService categoryService;
    private final CategoryRepository memoryCategoryRepository = new MemoryCategoryRepository();

    @BeforeEach
    void setUp() {
        categoryService = new CategoryService(memoryCategoryRepository);
    }

    @Test
    @DisplayName("카테고리명이 중복되지 않으면 카테고리 추가 성공")
    void categoryNameNotDuplicate_thenSuccess() {
        assertDoesNotThrow(() -> {
            categoryService.registerCategory("치킨");
        });
    }

    @Test
    @DisplayName("카테고리명이 중복되면 카테고리 추가 실패")
    void categoryNameDuplicate_thenFail() {
        memoryCategoryRepository.save(CategoryJpaEntity.builder()
            .name("치킨")
            .build());

        assertThrows(DuplicateCategoryException.class, () -> {
            categoryService.registerCategory("치킨");
        });
    }

    @Test
    @DisplayName("3개의 카테고리를 저장하고 카테고리 목록을 조회하면, 목록의 size가 3이어야 함")
    void givenThreeCategories_whenFindAll_thenListSizeThree() {
        memoryCategoryRepository.save(CategoryJpaEntity.builder().name("치킨").build());
        memoryCategoryRepository.save(CategoryJpaEntity.builder().name("피자").build());
        memoryCategoryRepository.save(CategoryJpaEntity.builder().name("야식").build());

        List<String> result = categoryService.findAllCategory();

        Assertions.assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("이름에 해당하는 카테고리가 없으면, 카테고리 수정 실패")
    void givenWrongPrevCategoryName_whenUpdateCategory_thenFail() {
        assertThrows(CategoryNotExistsException.class, () -> {
            categoryService.updateCategory("치킨", "중식");
        });
    }

    @Test
    @DisplayName("이름에 해당하는 카테고리가 존재하면, 카테고리 수정 성공")
    void whenUpdateCategory_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        memoryCategoryRepository.save(category);

        assertDoesNotThrow(() -> {
            categoryService.updateCategory("치킨", "중식");
        });
    }

    @Test
    @DisplayName("이름에 해당하는 카테고리가 없으면, 카테고리 삭제 실패")
    void givenWrongCategoryName_whenDeleteCategory_thenFail() {
        assertThrows(CategoryNotExistsException.class, () -> {
            categoryService.deleteCategory("치킨");
        });
    }

    @Test
    @DisplayName("이름에 해당하는 카테고리가 존재하면, 카테고리 삭제 성공")
    void whenDeleteCategory_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        memoryCategoryRepository.save(category);

        assertDoesNotThrow(() -> {
            categoryService.deleteCategory("치킨");
        });
    }
}
