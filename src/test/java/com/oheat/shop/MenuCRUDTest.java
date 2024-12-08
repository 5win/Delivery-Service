package com.oheat.shop;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuCRUDTest {

    @Disabled
    @Test
    @DisplayName("메뉴 등록 시, 매장id에 해당하는 매장이 없다면 메뉴 등록 실패")
    void givenMenuWithoutShopId_whenAddNewMenu_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 저장 시, 옵션 그룹 정보가 없다면 메뉴 등록 실패")
    void givenMenuWithoutOptionGroup_whenAddNewMenu_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 저장 시, 옵션이 존재하지 않는 옵션 그룹이 있다면 메뉴 등록 실패")
    void givenMenuWithEmptyOptionGroup_whenAddNewMenu_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("메뉴 저장 시, 1개 이상의 옵션 그룹이 존재하고 각 옵션 그룹에 옵션이 1개 이상이라면 메뉴 등록 성공")
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
