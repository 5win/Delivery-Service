package com.oheat.shop.jpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.OptionGroupJpaEntity;
import com.oheat.shop.entity.OptionJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.repository.CategoryJpaRepository;
import com.oheat.shop.repository.MenuJpaRepository;
import com.oheat.shop.repository.OptionGroupJpaRepository;
import com.oheat.shop.repository.OptionJpaRepository;
import com.oheat.shop.repository.ShopJpaRepository;
import jakarta.persistence.EntityManager;
import java.util.List;
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
    private OptionGroupJpaRepository optionGroupJpaRepository;
    @Autowired
    private OptionJpaRepository optionJpaRepository;
    @Autowired
    private EntityManager entityManager;

    @AfterEach
    void tearDown() {
        entityManager.createNativeQuery("ALTER TABLE shop ALTER COLUMN id RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE menu ALTER COLUMN id RESTART WITH 1")
            .executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE option_group ALTER COLUMN id RESTART WITH 1")
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

    @Test
    @DisplayName("존재하지 않는 메뉴에 옵션 그룹을 추가하면 실패")
    void givenOptionGroupWithWrongMenuId_whenAddNewOptionGroup_thenFail() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            optionGroupJpaRepository.save(OptionGroupJpaEntity.builder()
                .name("부분육 선택")
                .menuId(1L)
                .build());
        });
        entityManager.clear();
    }

    @Test
    @DisplayName("존재하지 않는 옵션 그룹에 옵션을 추가하면 실패")
    void givenOptionWithWrongOptionGroupId_whenAddNewOption_thenFail() {
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> {
            optionJpaRepository.save(OptionJpaEntity.builder()
                .name("순살")
                .optionGroupId(1L)
                .build());
        });
        entityManager.clear();
    }

    @Test
    @DisplayName("카테고리, 매장, 메뉴, 옵션 그룹, 옵션을 차례로 알맞게 저장하면 모두 저장 성공")
    void givenShopAndMenuAndOptionGroupAndOption_whenAddNewMenu_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        Assertions.assertDoesNotThrow(() -> {
            categoryJpaRepository.save(category);
            shopJpaRepository.save(ShopJpaEntity.builder().name("bbq").category(category).build());
            menuJpaRepository.save(MenuJpaEntity.builder().name("황올").shopId(1L).build());
            optionGroupJpaRepository.save(
                OptionGroupJpaEntity.builder().name("부분육 선택").menuId(1L).build());
            optionJpaRepository.save(
                OptionJpaEntity.builder().name("순살").optionGroupId(1L).build());
        });
    }

    @Test
    @DisplayName("각각 옵션이 2개인 옵션 그룹을 2개 가진 메뉴를 조회하면 총 옵션 개수는 4개임")
    void givenTwoOptionGroupsWithTwoOptions_whenFindMenu_thenTotalOptionFour() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        categoryJpaRepository.save(category);
        shopJpaRepository.save(ShopJpaEntity.builder().name("bbq").category(category).build());
        menuJpaRepository.save(MenuJpaEntity.builder().name("황올").shopId(1L).build());
        // 옵션 그룹 2개
        optionGroupJpaRepository.save(
            OptionGroupJpaEntity.builder().name("부분육 선택").menuId(1L).build());
        optionGroupJpaRepository.save(
            OptionGroupJpaEntity.builder().name("음료 추가 선택").menuId(1L).build());
        // 각 옵션 그룹마다 옵션 2개씩
        optionJpaRepository.save(
            OptionJpaEntity.builder().name("순살").optionGroupId(1L).build());
        optionJpaRepository.save(
            OptionJpaEntity.builder().name("콤보 변경").optionGroupId(1L).build());
        optionJpaRepository.save(
            OptionJpaEntity.builder().name("콜라").optionGroupId(2L).build());
        optionJpaRepository.save(
            OptionJpaEntity.builder().name("스프라이트").optionGroupId(2L).build());

        entityManager.clear();
        List<OptionGroupJpaEntity> result = menuJpaRepository.findById(1L).get()
            .getOptionGroups();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getOptions().size() + result.get(1).getOptions().size())
            .isEqualTo(4);
    }
}
