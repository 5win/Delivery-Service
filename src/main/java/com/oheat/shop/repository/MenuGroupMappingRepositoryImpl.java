package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class MenuGroupMappingRepositoryImpl implements MenuGroupMappingRepository {

    private final MenuGroupMappingJpaRepository menuGroupMappingJpaRepository;

    @Override
    public void save(MenuGroupMappingJpaEntity menuGroupMapping) {
        menuGroupMappingJpaRepository.save(menuGroupMapping);
    }

    @Override
    public Optional<MenuGroupMappingJpaEntity> findById(Long menuGroupMappingId) {
        return menuGroupMappingJpaRepository.findById(menuGroupMappingId);
    }

    @Override
    public void deleteById(Long menuGroupMappingId) {
        menuGroupMappingJpaRepository.deleteById(menuGroupMappingId);
    }
}
