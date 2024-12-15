package com.oheat.food.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.food.dto.MenuSaveRequest;
import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionGroupSaveRequest;
import com.oheat.food.dto.OptionGroupUpdateRequest;
import com.oheat.food.dto.OptionSaveRequest;
import com.oheat.food.dto.OptionUpdateRequest;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.DuplicateMenuException;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.NoOptionException;
import com.oheat.food.exception.NoOptionGroupException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.fake.MemoryMenuRepository;
import com.oheat.food.fake.MemoryShopRepository;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.ShopRepository;
import com.oheat.food.service.MenuService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuCRUDTest {

    private MenuService menuService;
    private final MenuRepository memoryMenuRepository = new MemoryMenuRepository();
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();

    @BeforeEach
    void setUp() {
        menuService = new MenuService(memoryMenuRepository, memoryShopRepository);
    }

    @Test
    @DisplayName("메뉴 등록 시, 매장id에 해당하는 매장이 없다면 메뉴 등록 실패")
    void givenMenuWithoutShopId_whenAddNewMenu_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황금올리브")
                .shopId(1L).
                build());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시, 옵션 그룹 정보가 없다면 메뉴 등록 실패")
    void givenMenuWithoutOptionGroup_whenAddNewMenu_thenFail() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        Assertions.assertThrows(NoOptionGroupException.class, () -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황올")
                .shopId(1L)
                .optionGroups(List.of())
                .build());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시, 옵션이 존재하지 않는 옵션 그룹이 있다면 메뉴 등록 실패")
    void givenMenuWithEmptyOptionGroup_whenAddNewMenu_thenFail() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());
        OptionGroupSaveRequest optionGroup = OptionGroupSaveRequest.builder()
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of())
            .build();

        Assertions.assertThrows(NoOptionException.class, () -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황올")
                .shopId(1L)
                .optionGroups(List.of(optionGroup))
                .build());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시, 1개 이상의 옵션 그룹이 존재하고 각 옵션 그룹에 옵션이 1개 이상이라면 메뉴 등록 성공")
    void givenMenuWithNotEmptyOptionGroup_whenAddNewMenu_thenSuccess() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        OptionGroupSaveRequest optionGroup = OptionGroupSaveRequest.builder()
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of(OptionSaveRequest.builder()
                .name("순살")
                .price(2_000)
                .build()))
            .build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황금올리브")
                .shopId(1L)
                .optionGroups(List.of(optionGroup))
                .build());
        });
    }

    @Test
    @DisplayName("메뉴 등록 시, 이미 메뉴가 있다면 메뉴 등록 실패")
    void givenDuplicateMenu_whenAddNewMenu_thenFail() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        OptionGroupSaveRequest optionGroup = OptionGroupSaveRequest.builder()
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of(OptionSaveRequest.builder()
                .name("순살")
                .price(2_000)
                .build()))
            .build();
        menuService.registerMenu(MenuSaveRequest.builder()
            .name("황금올리브").shopId(1L).optionGroups(List.of(optionGroup)).build());

        Assertions.assertThrows(DuplicateMenuException.class, () -> {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황금올리브").shopId(1L).optionGroups(List.of(optionGroup)).build());
        });
    }

    @Test
    @DisplayName("매장에 3개의 메뉴를 등록하면, 메뉴 DB의 size가 3이어야 함")
    void givenThreeMenu_whenAddNewMenu_thenListSizeThree() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        OptionGroupSaveRequest optionGroup = OptionGroupSaveRequest.builder()
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of(OptionSaveRequest.builder()
                .name("순살")
                .price(2_000)
                .build()))
            .build();

        for (int i = 0; i < 3; i++) {
            menuService.registerMenu(MenuSaveRequest.builder()
                .name("황올 세트 " + i + "번")
                .shopId(1L)
                .optionGroups(List.of(optionGroup))
                .build());
        }

        List<MenuJpaEntity> result = ((MemoryMenuRepository) memoryMenuRepository).findAll();

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("매장에 3개의 메뉴를 등록한 뒤 매장 엔티티를 조회하면, 매장 엔티티의 메뉴 리스트 사이즈가 3이어야 함")
    void givenThreeMenu_whenAddNewMenuAndFindShopById_thenMenuListAtShopIsSizeThree() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        for (int i = 0; i < 3; i++) {
            shop.getMenuSet().add(MenuJpaEntity.builder()
                .name("황올 세트 " + i + "번")
                .shop(shop)
                .build());
        }
        memoryShopRepository.save(shop);

        List<MenuJpaEntity> result = menuService.findByShopId(1L);

        assertThat(result.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("해당 매장이 존재하지 않으면, 메뉴 수정 실패")
    void givenWrongShopId_whenUpdateMenu_thenFail() {
        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            menuService.updateMenu(MenuUpdateRequest.builder()
                .menuId(1L)
                .name("황금올리브")
                .build());
        });
    }

    @Test
    @DisplayName("옵션 그룹과 옵션 값이 존재하면, 기존 메뉴 삭제 후 수정된 메뉴로 저장")
    void givenMenuWithNotEmptyOptionGroup_whenUpdateMenu_thenSuccess() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();

        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);

        // update
        OptionGroupUpdateRequest optionGroupUpdateRequest = OptionGroupUpdateRequest.builder()
            .name("부분육 선택")
            .required(true)
            .maxNumOfSelect(3)
            .options(List.of(OptionUpdateRequest.builder()
                .name("닭다리")
                .price(99_000)
                .build()))
            .build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.updateMenu(MenuUpdateRequest.builder()
                .menuId(1L)
                .shopId(1L)
                .name("양념치킨")
                .optionGroups(List.of(optionGroupUpdateRequest))
                .build());
        });
        assertThat(memoryMenuRepository.findById(1L).get().getName())
            .isEqualTo("양념치킨");
    }

    @Test
    @DisplayName("메뉴가 존재하지 않으면, 메뉴 삭제 실패")
    void givenMenuNotExists_whenDeleteMenu_thenFail() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();

        memoryShopRepository.save(shop);

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            menuService.deleteById(1L);
        });
    }

    @Test
    @DisplayName("메뉴가 존재하면, 메뉴 삭제 성공")
    void givenMenu_whenDeleteMenu_thenSuccess() {
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황금올리브").build();

        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);

        Assertions.assertDoesNotThrow(() -> {
            menuService.deleteById(1L);
        });
    }
}
