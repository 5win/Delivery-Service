package com.oheat.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class LoginRequest {

    private final String username;
    private final String password;
}
