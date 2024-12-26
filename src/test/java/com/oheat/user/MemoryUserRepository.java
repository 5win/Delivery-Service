package com.oheat.user;

import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryUserRepository implements UserRepository {

    private final Map<Long, UserJpaEntity> users = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(UserJpaEntity user) {
        users.put(autoId++, user);
    }

    @Override
    public Optional<UserJpaEntity> findByUsername(String username) {
        return users.values().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst();
    }
}
