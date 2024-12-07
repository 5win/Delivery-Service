package com.oheat.shop;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.repository.CategoryJpaRepository;
import com.oheat.shop.repository.ShopJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class ShopRepositoryTest {

    @Autowired
    private ShopJpaRepository shopJpaRepository;
    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Test
    @DisplayName("매장 등록 정보에 카테고리가 null이면 등록 실패")
    void shopNotContainsCategory_thenFail() {
        assertThrows(DataIntegrityViolationException.class, () -> {
            shopJpaRepository.save(
                ShopJpaEntity.builder()
                    .name("bbq")
                    .minimumOrderAmount(14_000)
                    .build()
            );
        });
    }

    @Test
    @DisplayName("이름이 중복되지 않으면, 매장 등록 성공")
    void shopNameNotDuplicate_thenSuccess() {
        categoryJpaRepository.save(
            CategoryJpaEntity.builder()
                .name("치킨")
                .build()
        );
        CategoryJpaEntity category = categoryJpaRepository.findByName("치킨").get();

        assertDoesNotThrow(() -> {
            shopJpaRepository.save(
                ShopJpaEntity.builder()
                    .name("bbq")
                    .category(category)
                    .minimumOrderAmount(14_000)
                    .build());
        });
    }

    @Test
    @DisplayName("이름이 중복되면, 매장 등록 실패")
    void shopNameDuplicate_thenFail() {
        categoryJpaRepository.save(
            CategoryJpaEntity.builder()
                .name("치킨")
                .build()
        );
        CategoryJpaEntity category = categoryJpaRepository.findByName("치킨").get();

        shopJpaRepository.save(
            ShopJpaEntity.builder()
                .name("bbq")
                .category(category)
                .minimumOrderAmount(14_000)
                .build());

        assertThrows(DataIntegrityViolationException.class, () -> {
            shopJpaRepository.save(
                ShopJpaEntity.builder()
                    .name("bbq")
                    .category(category)
                    .minimumOrderAmount(14_000)
                    .build());
        });
    }
}
