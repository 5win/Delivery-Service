package com.oheat.user.dto;

import com.oheat.user.constant.Role;
import com.oheat.user.entity.UserJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class UserSaveRequest {

    private final String username;
    private final String password;
    private final String address;
    private final Role role;

    public UserJpaEntity toEntity() {
        return UserJpaEntity.builder()
            .username(this.username)
            .password(this.password)
            .address(this.address)
            .role(this.role)
            .build();
    }
}