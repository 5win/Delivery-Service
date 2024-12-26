package com.oheat.user.repository;

import com.oheat.user.entity.UserJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(UserJpaEntity user) {
        userJpaRepository.save(user);
    }

    @Override
    public Optional<UserJpaEntity> findByUsername(String username) {
        return userJpaRepository.findByUsername(username);
    }
}
