package com.oheat.user.repository;

import com.oheat.user.entity.UserJpaEntity;
import java.util.Optional;

public interface UserRepository {

    void save(UserJpaEntity user);

    Optional<UserJpaEntity> findByUsername(String username);
}
