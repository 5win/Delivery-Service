package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuGroupJpaEntity;
import java.util.Optional;

public interface MenuGroupRepository {

    void save(MenuGroupJpaEntity menuGroup);

    Optional<MenuGroupJpaEntity> findById(Long menuGroupId);

    void deleteById(Long menuGroupId);
}
