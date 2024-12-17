package com.oheat.food.jpaTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionGroupUpdateRequest;
import com.oheat.food.dto.OptionUpdateRequest;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.repository.CategoryJpaRepository;
import com.oheat.food.repository.MenuJpaRepository;
import com.oheat.food.repository.ShopJpaRepository;
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
        entityManager.createNativeQuery("ALTER TABLE option ALTER COLUMN id RESTART WITH 1")
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
        ShopJpaEntity shop = shopJpaRepository.save(
            ShopJpaEntity.builder().name("bbq").category(category).build());

        Assertions.assertDoesNotThrow(() -> {
            menuJpaRepository.save(MenuJpaEntity.builder().name("황올").shop(shop).build());
        });
    }

    @Test
    @DisplayName("하나의 매장에 3개 메뉴를 저장한 뒤, 매장 엔티티를 조회하면 menuList의 size는 3이어야 함")
    void usingJpa_givenThreeMenu_whenAddNewMenu_thenListSizeThree() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        categoryJpaRepository.save(category);
        ShopJpaEntity shop = shopJpaRepository.save(ShopJpaEntity.builder()
            .name("bbq").category(category).build());

        for (int i = 0; i < 3; i++) {
            menuJpaRepository.save(MenuJpaEntity.builder()
                .name("황올 세트 " + i + "번")
                .shop(shop)
                .build());
        }
        entityManager.clear();

        ShopJpaEntity result = shopJpaRepository.findById(1L).get();

        assertThat(result.getMenuSet().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("카테고리, 매장, 메뉴, 옵션 그룹, 옵션을 차례로 알맞게 저장하면 모두 저장 성공")
    void givenShopAndMenuAndOptionGroupAndOption_whenAddNewMenu_thenSuccess() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").category(category).build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").shop(shop).build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .name("부분육 선택").menu(menu).build();
        OptionJpaEntity option = OptionJpaEntity.builder()
            .name("순살").optionGroup(optionGroup).build();

        categoryJpaRepository.save(category);
        shopJpaRepository.save(shop);

        // 저장 과정에서 예외 발생 X
        Assertions.assertDoesNotThrow(() -> {
            optionGroup.addOption(option);
            menu.addOptionGroup(optionGroup);
            menuJpaRepository.save(menu);
        });
        entityManager.clear();

        // 옵션 그룹과 옵션 저장 확인
        OptionGroupJpaEntity optionGroupResult = menuJpaRepository.findById(1L).get()
            .getOptionGroups().getFirst();
        OptionJpaEntity optionResult = optionGroupResult.getOptions().getFirst();

        assertThat(optionGroupResult.getName()).isEqualTo("부분육 선택");
        assertThat(optionResult.getName()).isEqualTo("순살");
    }

    @Test
    @DisplayName("각각 옵션이 2개인 옵션 그룹을 2개 가진 메뉴를 조회하면 총 옵션 개수는 4개임")
    void givenTwoOptionGroupsWithTwoOptions_whenFindMenu_thenTotalOptionFour() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").category(category).build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").shop(shop).build();
        OptionGroupJpaEntity optionGroup1 = OptionGroupJpaEntity.builder()
            .name("부분육 선택").menu(menu).build();
        OptionGroupJpaEntity optionGroup2 = OptionGroupJpaEntity.builder()
            .name("음료 추가 선택").menu(menu).build();
        OptionJpaEntity option1 = OptionJpaEntity.builder()
            .name("순살").optionGroup(optionGroup1).build();
        OptionJpaEntity option2 = OptionJpaEntity.builder()
            .name("콤보 변경").optionGroup(optionGroup1).build();
        OptionJpaEntity option3 = OptionJpaEntity.builder()
            .name("콜라").optionGroup(optionGroup2).build();
        OptionJpaEntity option4 = OptionJpaEntity.builder()
            .name("스프라이트").optionGroup(optionGroup2).build();

        categoryJpaRepository.save(category);
        shopJpaRepository.save(shop);

        // 각 옵션 그룹마다 옵션 2개씩
        optionGroup1.addOption(option1);
        optionGroup1.addOption(option2);
        optionGroup2.addOption(option3);
        optionGroup2.addOption(option4);
        // 옵션 그룹 2개
        menu.addOptionGroup(optionGroup1);
        menu.addOptionGroup(optionGroup2);

        menuJpaRepository.save(menu);
        entityManager.clear();

        List<OptionGroupJpaEntity> result = menuJpaRepository.findById(1L).get()
            .getOptionGroups();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getOptions().size() + result.get(1).getOptions().size())
            .isEqualTo(4);
    }

    @Test
    @DisplayName("메뉴를 삭제하면, 옵션 그룹과 옵션도 삭제됨")
    void whenDeleteMenu_thenDeleteOptionGroupAndOption() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").category(category).build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").shop(shop).build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .name("부분육 선택").menu(menu).build();
        OptionJpaEntity option = OptionJpaEntity.builder()
            .name("순살").optionGroup(optionGroup).build();

        categoryJpaRepository.save(category);
        shopJpaRepository.save(shop);

        optionGroup.addOption(option);
        menu.addOptionGroup(optionGroup);
        menuJpaRepository.save(menu);
        menuJpaRepository.delete(menu);

        List<MenuJpaEntity> menuResult = menuJpaRepository.findAll();
        List<OptionGroupJpaEntity> optionGroupResult = optionGroupJpaRepository.findAll();
        List<OptionJpaEntity> optionResult = optionJpaRepository.findAll();

        assertThat(menuResult.size()).isZero();
        assertThat(optionGroupResult.size()).isZero();
        assertThat(optionResult.size()).isZero();
    }

    @Test
    @DisplayName("메뉴의 옵션 그룹을 바꾸면, 기존 옵션 그룹은 삭제되고 새로운 옵션 그룹이 추가됨")
    void whenUpdateOptionGroup_thenDeletePrevOptionGroupAndAddNewOptionGroup() {
        CategoryJpaEntity category = CategoryJpaEntity.builder().name("치킨").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").category(category).build();
        MenuJpaEntity menu1 = MenuJpaEntity.builder().name("황올").shop(shop).build();
        OptionGroupJpaEntity optionGroup1 = OptionGroupJpaEntity.builder()
            .name("부분육 선택").menu(menu1).build();
        OptionJpaEntity option1 = OptionJpaEntity.builder()
            .name("순살").optionGroup(optionGroup1).build();

        categoryJpaRepository.save(category);
        shopJpaRepository.save(shop);

        // 처음 옵션으로 저장
        optionGroup1.addOption(option1);
        menu1.addOptionGroup(optionGroup1);
        menuJpaRepository.save(menu1);
        entityManager.clear();

        // 수정된 옵션으로 저장
        OptionGroupUpdateRequest optionGroupUpdateRequest = OptionGroupUpdateRequest.builder()
            .name("음료 선택")
            .required(true)
            .maxNumOfSelect(3)
            .options(List.of(OptionUpdateRequest.builder()
                .name("콜라")
                .price(99_000)
                .build()))
            .build();
        MenuUpdateRequest updateRequest = MenuUpdateRequest.builder()
            .menuId(1L)
            .name("양념치킨")
            .optionGroups(List.of(optionGroupUpdateRequest))
            .build();

        menu1.updateMenu(updateRequest);
        menuJpaRepository.save(menu1);
        entityManager.flush();      // 더티 체킹을 위해 flush

        // 수정 이전의 내용이 삭제되었는지 확인
        assertThat(optionGroupJpaRepository.findAll().size()).isEqualTo(1);
        assertThat(optionJpaRepository.findAll().size()).isEqualTo(1);
        // 수정 내용이 반영되었는지 확인
        OptionGroupJpaEntity optionGroupResult = menuJpaRepository.findById(1L).get()
            .getOptionGroups().getFirst();
        OptionJpaEntity optionResult = optionGroupResult.getOptions().getFirst();
        assertThat(optionGroupResult.getName()).isEqualTo("음료 선택");
        assertThat(optionResult.getName()).isEqualTo("콜라");
    }
}
