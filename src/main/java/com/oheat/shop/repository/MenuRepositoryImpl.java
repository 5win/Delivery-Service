package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuRepositoryImpl implements MenuRepository {

    private final MenuJpaRepository menuJpaRepository;

    @Override
    public void save(MenuJpaEntity menuJpaEntity) {
        menuJpaRepository.save(menuJpaEntity);
    }
}
