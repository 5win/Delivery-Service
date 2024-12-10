package com.oheat.shop.jpaTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MenuGroupRepositoryTest {

    @Test
    @DisplayName("shopId에 해당하는 매장이 없으면, 메뉴 그룹 저장 실패")
    void usingJpa_givenWrongShopId_whenAddMenuGroup_thenFail() {

    }

    @Test
    @DisplayName("매장에 menuId에 해당하는 메뉴가 없으면, 메뉴 그룹 저장 실패")
    void usingJpa_givenWrongMenuId_whenAddMenuGroup_thenFail() {

    }

    @Test
    @DisplayName("매장이 존재하며 메뉴가 매장에 존재한다면, 메뉴 그룹 저장 성공")
    void usingJpa_givenShopIdAndMenuId_whenAddMenuGroup_thenSuccess() {

    }

    @Test
    @DisplayName("매장에 메뉴 그룹이 2개 있다면, 메뉴 그룹을 전체 조회했을 때 그룹 개수가 2개여야 함")
    void usingJpa_givenTwoMenuGroup_whenFindAllMenuGroup_thenReturnTwoMenuGroup() {

    }

    @Test
    @DisplayName("후라이드 메뉴 그룹에 치킨 메뉴가 2개 있다면, 메뉴 그룹을 통해 메뉴 2개가 조회되어야 함")
    void usingJpa_givenMenuGroupWithTwoMenu_whenFindMenuInMenuGroup_thenReturnTwoMenu() {

    }

}
