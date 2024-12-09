package com.oheat.shop.serviceTest;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.shop.dto.MenuFindByShopIdResponse;
import com.oheat.shop.dto.MenuSaveRequest;
import com.oheat.shop.dto.OptionGroupSaveRequest;
import com.oheat.shop.dto.OptionSaveRequest;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.NoOptionException;
import com.oheat.shop.exception.NoOptionGroupException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.fake.MemoryMenuRepository;
import com.oheat.shop.fake.MemoryShopRepository;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import com.oheat.shop.service.MenuService;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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
            menuService.save(MenuSaveRequest.builder()
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
            menuService.save(MenuSaveRequest.builder()
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
            .menuId(1L)
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of())
            .build();

        Assertions.assertThrows(NoOptionException.class, () -> {
            menuService.save(MenuSaveRequest.builder()
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
            .menuId(1L)
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of(OptionSaveRequest.builder()
                .name("순살")
                .price(2_000)
                .optionGroupId(1L)
                .build()))
            .build();

        Assertions.assertDoesNotThrow(() -> {
            menuService.save(MenuSaveRequest.builder()
                .name("황금올리브")
                .shopId(1L)
                .optionGroups(List.of(optionGroup))
                .build());
        });
    }

    @Test
    @DisplayName("매장에 3개의 메뉴를 등록하면, 메뉴 DB의 size가 3이어야 함")
    void givenThreeMenu_whenAddNewMenu_thenListSizeThree() {
        memoryShopRepository.save(ShopJpaEntity.builder().name("bbq").build());

        OptionGroupSaveRequest optionGroup = OptionGroupSaveRequest.builder()
            .name("부분육 선택")
            .menuId(1L)
            .required(true)
            .maxNumOfSelect(1)
            .options(List.of(OptionSaveRequest.builder()
                .name("순살")
                .price(2_000)
                .optionGroupId(1L)
                .build()))
            .build();

        for (int i = 0; i < 3; i++) {
            menuService.save(MenuSaveRequest.builder()
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
            shop.getMenuList().add(MenuJpaEntity.builder()
                .name("황올 세트 " + i + "번")
                .shopId(1L)
                .build());
        }
        memoryShopRepository.save(shop);

        List<MenuFindByShopIdResponse> result = menuService.findByShopId(1L);

        assertThat(result.size()).isEqualTo(3);
    }

    @Disabled
    @Test
    @DisplayName("메뉴 그룹에 메뉴 추가 시, 메뉴가 해당 매장에 속하지 않으면 추가 실패")
    void givenShopIdAndWrongMenuId_whenAddToMenuGroup_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 그룹에 이미 추가된 메뉴이면, 메뉴 그룹에 메뉴 추가 실패")
    void givenShopIdAndDuplicateMenu_whenAddToMenuGroup_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 그룹에 메뉴 추가 시, 메뉴가 해당 매장에 속하면서 중복되지 않으면 추가 성공")
    void givenShopIdAndMenuId_whenAddToMenuGroup_thenSuccess() {

    }
}
