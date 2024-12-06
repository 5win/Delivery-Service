package com.oheat.shop;

import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.shop.dto.ShopSaveRequest;
import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.CategoryNotExistsException;
import com.oheat.shop.exception.DuplicateCategoryException;
import com.oheat.shop.exception.DuplicateShopNameException;
import com.oheat.shop.service.CategoryService;
import com.oheat.shop.service.ShopService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShopServiceTest {

    private ShopService shopService;
    private CategoryService categoryService;
    private final MemoryShopRepository memoryShopRepository = new MemoryShopRepository();
    private final MemoryCategoryRepository memoryCategoryRepository = new MemoryCategoryRepository();

    @BeforeEach
    void setUp() {
        shopService = new ShopService(memoryShopRepository, memoryCategoryRepository);
        categoryService = new CategoryService(memoryCategoryRepository);
    }

    @Test
    @DisplayName("매장 이름이 중복되면 매장 등록 실패")
    void shopNameDuplicate_thenFail() {
        memoryShopRepository.save(
            ShopJpaEntity.builder()
                .shopName("오잇")
                .build());

        assertThrows(DuplicateShopNameException.class, () -> {
            shopService.registerShop(
                ShopSaveRequest.builder()
                    .shopName("오잇")
                    .build());
        });
    }

    @Test
    @DisplayName("카테고리명이 중복되면 카테고리 추가 실패")
    void categoryNameDuplicate_thenFail() {
        memoryCategoryRepository.save(CategoryJpaEntity.builder()
            .category("치킨")
            .build());

        assertThrows(DuplicateCategoryException.class, () -> {
            categoryService.registerCategory("치킨");
        });
    }

    @Test
    @DisplayName("카테고리가 없으면 매장 등록 실패")
    void whenCategoryNotExists_thenFail() {

        assertThrows(CategoryNotExistsException.class, () -> {
            shopService.registerShop(ShopSaveRequest.builder()
                .shopName("bbq")
                .categoryName("치킨")
                .minimumOrderAmount(14_000)
                .build());
        });
    }

    @Disabled
    @Test
    @DisplayName("매장 이름에 숫자, 한글, 영어 이외의 글자가 포함되면 실패")
    void shopNameContainsOnlyNumberKoreanEnglish() {

    }

}
