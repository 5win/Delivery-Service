package com.oheat.food.repository;

import com.oheat.food.entity.MenuGroupMappingJpaEntity;
import java.util.Optional;

public interface MenuGroupMappingRepository {

    void save(MenuGroupMappingJpaEntity menuGroupMapping);

    Optional<MenuGroupMappingJpaEntity> findById(Long menuGroupMappingId);

    void deleteById(Long menuGroupMappingId);
}
