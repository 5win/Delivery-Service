package com.oheat.shop;

import com.oheat.shop.dto.MenuSaveRequest;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import com.oheat.shop.service.MenuService;
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

    @Disabled
    @Test
    @DisplayName("메뉴 등록 시, 매장이 존재하면 메뉴 등록 성공")
    void givenMenuWithShopId_whenAddNewMenu_thenSuccess() {

    }

    @Disabled
    @Test
    @DisplayName("매장에 3개의 메뉴를 등록하면, 매장의 메뉴 조회 시 리스트 size가 3이어야 함")
    void givenThreeMenu_whenAddNewMenu_thenListSizeThree() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 등록 시, 옵션 그룹 정보가 없다면 메뉴 등록 실패")
    void givenMenuWithoutOptionGroup_whenAddNewMenu_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 등록 시, 옵션이 존재하지 않는 옵션 그룹이 있다면 메뉴 등록 실패")
    void givenMenuWithEmptyOptionGroup_whenAddNewMenu_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 등록 시, 1개 이상의 옵션 그룹이 존재하고 각 옵션 그룹에 옵션이 1개 이상이라면 메뉴 등록 성공")
    void givenMenuWithNotEmptyOptionGroup_whenAddNewMenu_thenSuccess() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴를 메뉴 그룹에 추가 시, 메뉴가 해당 매장에 속하지 않으면 추가 실패")
    void givenShopIdAndWrongMenuId_whenAddToMenuGroup_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴를 메뉴 그룹에 추가 시, 메뉴가 해당 매장에 속하면 추가 성공")
    void givenShopIdAndMenuId_whenAddToMenuGroup_thenSuccess() {

    }
}
