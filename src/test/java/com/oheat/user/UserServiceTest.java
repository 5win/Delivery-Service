package com.oheat.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.common.JwtUtils;
import com.oheat.user.constant.Role;
import com.oheat.user.dto.LoginRequest;
import com.oheat.user.dto.UserSaveRequest;
import com.oheat.user.exception.DuplicateUserException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserServiceTest {

    private final UserRepository memoryUserRepository = new MemoryUserRepository();
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(memoryUserRepository);
    }

    @Test
    @DisplayName("username이 이미 존재하면 DuplicateUserException 예외를 던지고 회원가입을 실패한다")
    void givenDuplicateId_whenRegisterUser_thenThrowDuplicateUserException() {
        UserSaveRequest saveRequest = UserSaveRequest.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();

        userService.registerUser(saveRequest);
        Assertions.assertThrows(DuplicateUserException.class, () -> {
            userService.registerUser(saveRequest);
        });
    }

    @Test
    @DisplayName("username 사용이 가능하면 회원가입에 성공한다")
    void givenNotDuplicateId_whenRegisterUser_thenSuccess() {
        UserSaveRequest saveRequest = UserSaveRequest.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();

        Assertions.assertDoesNotThrow(() -> {
            userService.registerUser(saveRequest);
        });
    }

    @Test
    @DisplayName("로그인 시, 존재하지 않는 username이면 UserNotExistsException 예외를 던진다")
    void givenIdNotExists_whenLogin_thenThrowUserNotExistsException() {
        LoginRequest loginRequest = LoginRequest.builder().username("sgoh").password("pw").build();

        Assertions.assertThrows(UserNotExistsException.class, () -> {
            userService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("username, PW가 일치하면 로그인에 성공한다")
    void givenValidIdPw_whenLogin_thenSuccess() {
        UserSaveRequest saveRequest = UserSaveRequest.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        LoginRequest loginRequest = LoginRequest.builder().username("sgoh").password("pw").build();

        userService.registerUser(saveRequest);
        Assertions.assertDoesNotThrow(() -> {
            userService.login(loginRequest);
        });
    }

    @Test
    @DisplayName("로그인을 성공하면 JWT 토큰을 반환하고, JWT를 파싱한 username과 role이 유저 정보와 일치한다")
    void whenLoginSuccess_thenReturnJwtWithValidUserInfo() {
        UserSaveRequest saveRequest = UserSaveRequest.builder()
            .username("sgoh")
            .password("pw")
            .role(Role.ADMIN)
            .build();
        LoginRequest loginRequest = LoginRequest.builder().username("sgoh").password("pw").build();

        userService.registerUser(saveRequest);

        String token = userService.login(loginRequest);
        String username = JwtUtils.getUsername(token);
        Role userRole = JwtUtils.getUserRole(token);

        assertThat(username).isEqualTo("sgoh");
        assertThat(userRole).isEqualTo(Role.ADMIN);
    }
}
