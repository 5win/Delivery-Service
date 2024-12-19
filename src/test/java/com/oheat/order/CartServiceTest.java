package com.oheat.order;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartServiceTest {

    // Create Test
    @Disabled
    @Test
    @DisplayName("장바구니에 담을 때 매장이 존재하지 않으면, ShopNotExistsException")
    void test1() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담을 때 메뉴가 존재하지 않으면, MenuNotExistsException")
    void test2() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담을 때 옵션그룹이 존재하지 않으면, OptionGroupNotExistsException")
    void test3() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담을 때 옵션이 존재하지 않으면, OptionNotExistsException")
    void test4() {

    }

    @Disabled
    @Test
    @DisplayName("기존 장바구니에 다른 매장의 메뉴가 담겨있으면, OtherShopMenuAlreadyExistsException")
    void test5() {

    }

    @Disabled
    @Test
    @DisplayName("기존에 추가된 메뉴와 추가할 메뉴, 옵션그룹, 옵션 중 하나라도 다르면, 새로 장바구니에 추가한다")
    void test6() {

    }

    @Disabled
    @Test
    @DisplayName("옵션그룹과 옵션이 모두 같은 메뉴가 이미 추가되어 있으면, 기존 정보에서 개수만 증가시킨다")
    void test7() {

    }

    // Read Test
    @Disabled
    @Test
    @DisplayName("장바구니에 담은 메뉴, 옵션그룹, 옵션 이름, 개수를 메뉴 단위 목록으로 조회한다")
    void test8() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니 조회 시, 메뉴 목록에 각 금액이 함께 조회된다")
    void test9() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담은 메뉴들의 총 금액 합을 조회한다")
    void test10() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담긴 상태에서 메뉴나 옵션 정보가 수정되면, 수정된 정보로 조회한다")
    void test11() {

    }

    // Update Test
    @Disabled
    @Test
    @DisplayName("메뉴의 수정된 옵션으로 장바구니에 담긴 메뉴 정보를 변경한다")
    void test12() {

    }

    @Disabled
    @Test
    @DisplayName("수정된 정보와 동일한 메뉴가 장바구니에 이미 존재하면, 개수만 증가시킨다")
    void test13() {

    }

    // Delete Test
    @Disabled
    @Test
    @DisplayName("cartId에 해당하는 메뉴를 장바구니에서 삭제한다")
    void test14() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니의 메뉴를 모두 삭제한다")
    void test15() {

    }

    @Disabled
    @Test
    @DisplayName("매장, 메뉴, 옵션그룹, 옵션 중에 하나라도 삭제되면, 해당 정보를 담은 메뉴가 장바구니에서 삭제된다")
    void test16() {

    }
}
