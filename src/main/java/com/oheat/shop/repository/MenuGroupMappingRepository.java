package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import java.util.Optional;

public interface MenuGroupMappingRepository {

    void save(MenuGroupMappingJpaEntity menuGroupMapping);

    Optional<MenuGroupMappingJpaEntity> findById(Long menuGroupMappingId);

    void deleteById(Long menuGroupMappingId);
}
