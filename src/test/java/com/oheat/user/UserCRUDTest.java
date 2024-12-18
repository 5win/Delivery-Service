package com.oheat.user;

import com.oheat.user.constant.Role;
import com.oheat.user.dto.UserSaveRequest;
import com.oheat.user.exception.DuplicateUserException;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserCRUDTest {

    private final UserRepository memoryUserRepository = new MemoryUserRepository();
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(memoryUserRepository);
    }

    @Test
    @DisplayName("ID가 이미 존재하면 DuplicateUserException 예외를 던지고 회원가입을 실패한다")
    void givenDuplicateId_whenRegisterUser_thenThrowDuplicateUserException() {
        UserSaveRequest saveRequest = UserSaveRequest.builder()
            .username("sgoh")
            .password("pw")
            .address("서울특별시 광진구")
            .role(Role.ADMIN)
            .build();

        userService.registerUser(saveRequest);
        Assertions.assertThrows(DuplicateUserException.class, () -> {
            userService.registerUser(saveRequest);
        });
    }

    @Test
    @DisplayName("ID 사용이 가능하면 회원가입에 성공한다")
    void givenNotDuplicateId_whenRegisterUser_thenSuccess() {
        UserSaveRequest saveRequest = UserSaveRequest.builder()
            .username("sgoh")
            .password("pw")
            .address("서울특별시 광진구")
            .role(Role.ADMIN)
            .build();

        Assertions.assertDoesNotThrow(() -> {
            userService.registerUser(saveRequest);
        });
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
