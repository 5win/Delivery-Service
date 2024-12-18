package com.oheat.user.service;

import com.oheat.user.dto.UserSaveRequest;
import com.oheat.user.exception.DuplicateUserException;
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
}
