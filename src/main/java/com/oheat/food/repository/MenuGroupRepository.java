package com.oheat.food.repository;

import com.oheat.food.entity.MenuGroupJpaEntity;
import java.util.Optional;

public interface MenuGroupRepository {

    void save(MenuGroupJpaEntity menuGroup);

    Optional<MenuGroupJpaEntity> findById(Long menuGroupId);

    void deleteById(Long menuGroupId);
}
