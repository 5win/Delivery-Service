package com.oheat.user.service;

import com.oheat.common.jwt.JwtUtils;
import com.oheat.user.dto.LoginRequest;
import com.oheat.user.dto.UserSaveRequest;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.DuplicateUserException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void registerUser(UserSaveRequest saveRequest) {
        userRepository.findByUsername(saveRequest.getUsername())
            .ifPresent(user -> {
                throw new DuplicateUserException();
            });

        userRepository.save(saveRequest.toEntity());
    }

    public String login(LoginRequest loginRequest) {
        UserJpaEntity user = userRepository.findByUsername(loginRequest.getUsername())
            .orElseThrow(UserNotExistsException::new);

        return JwtUtils.generateJwtToken(user);
    }
}
