package com.oheat.shop.jpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.repository.CategoryJpaRepository;
import com.oheat.shop.repository.MenuJpaRepository;
import com.oheat.shop.repository.ShopJpaRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
public class MenuRepositoryTest {

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;
    @Autowired
    private ShopJpaRepository shopJpaRepository;
    @Autowired
    private MenuJpaRepository menuJpaRepository;
    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        entityManager.createNativeQuery("ALTER TABLE shop ALTER COLUMN id RESTART WITH 1")
            .executeUpdate();
    }

    @Test
    @DisplayName("shopId에 해당하는 매장이 없으면, 메뉴 저장 실패")
    void usingJpa_givenMenuWithoutShopId_whenAddNewMenu_thenFail() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            menuJpaRepository.save(MenuJpaEntity.builder().name("황올").build());
        });

        entityManager.clear();
    }

    @Test
    @DisplayName("shopId에 해당하는 매장이 있으면, 메뉴 저장 성공")
    void usingJpa_givenMenuWithShopId_whenAddNewMenu_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        categoryJpaRepository.save(category);
        shopJpaRepository.save(ShopJpaEntity.builder().name("bbq").category(category).build());

        Assertions.assertDoesNotThrow(() -> {
            menuJpaRepository.save(MenuJpaEntity.builder().name("황올").shopId(1L).build());
        });
    }

    @Test
    @DisplayName("하나의 매장에 3개 메뉴를 저장한 뒤, 매장 엔티티를 조회하면 menuList의 size는 3이어야 함")
    void usingJpa_givenThreeMenu_whenAddNewMenu_thenListSizeThree() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        categoryJpaRepository.save(category);
        shopJpaRepository.save(ShopJpaEntity.builder().name("bbq").category(category).build());

        for (int i = 0; i < 3; i++) {
            menuJpaRepository.save(MenuJpaEntity.builder()
                .name("황올 세트 " + i + "번")
                .shopId(1L)
                .build());
        }
        entityManager.clear();

        ShopJpaEntity shop = shopJpaRepository.findById(1L).get();

        assertThat(shop.getMenuList().size()).isEqualTo(3);
    }
}