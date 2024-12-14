package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuJpaEntity;
import java.util.Optional;

public interface MenuRepository {

    void save(MenuJpaEntity menuJpaEntity);

    Optional<MenuJpaEntity> findById(Long menuId);

    Optional<MenuJpaEntity> findByName(String name);

    void deleteById(Long menuId);
}
