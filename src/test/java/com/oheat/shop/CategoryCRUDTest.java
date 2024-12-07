package com.oheat.shop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.exception.DuplicateCategoryException;
import com.oheat.shop.repository.CategoryRepository;
import com.oheat.shop.service.CategoryService;
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

        List<CategoryJpaEntity> result = memoryCategoryRepository.findAll();

        Assertions.assertThat(result.size()).isEqualTo(3);
    }

}
