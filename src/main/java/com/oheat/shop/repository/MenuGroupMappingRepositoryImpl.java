package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
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
}
