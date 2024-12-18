package com.oheat.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserCRUDTest {

    @Disabled
    @Test
    @DisplayName("ID가 이미 존재하면 회원가입을 실패한다")
    void givenDuplicateId_whenRegisterUser_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("ID 사용이 가능하면 회원가입에 성공한다")
    void givenNotDuplicateId_whenRegisterUser_thenSuccess() {

    }

    @Disabled
    @Test
    @DisplayName("존재하지 않는 ID이면 UserNotExistsException 예외를 던진다")
    void givenIdNotExists_whenLogin_thenThrowUserNotExistsException() {

    }

    @Disabled
    @Test
    @DisplayName("ID, PW가 일치하면 로그인에 성공하고, JWT를 반환한다")
    void givenValidIdPw_whenLogin_thenReturnJwt() {

    }

    @Disabled
    @Test
    @DisplayName("배달시킬 주소를 새로 등록한다")
    void whenRegisterAddress_thenSuccess() {

    }

    @Disabled
    @Test
    @DisplayName("배달 주소 목록을 조회한다")
    void whenFindAllAddress_thenReturnAddressList() {

    }

    @Disabled
    @Test
    @DisplayName("배달 주소 목록 중, 현재 선택된 배달 주소를 조회한다")
    void whenFindSelectedAddress_thenReturnSelectedAddress() {

    }

    @Disabled
    @Test
    @DisplayName("배달 주소 목록 중, 기존에 등록했던 다른 주소를 선택하여 주소 설정 값을 변경한다")
    void whenChangeSelectedAddress_thenSuccess() {

    }
}
