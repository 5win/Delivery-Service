package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuGroupJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuGroupRepositoryImpl implements MenuGroupRepository {

    private final MenuGroupJpaRepository menuGroupJpaRepository;

    @Override
    public void save(MenuGroupJpaEntity menuGroup) {
        menuGroupJpaRepository.save(menuGroup);
    }

    @Override
    public Optional<MenuGroupJpaEntity> findById(Long menuGroupId) {
        return menuGroupJpaRepository.findById(menuGroupId);
    }

    @Override
    public void deleteById(Long menuGroupId) {
        menuGroupJpaRepository.deleteById(menuGroupId);
    }
}
