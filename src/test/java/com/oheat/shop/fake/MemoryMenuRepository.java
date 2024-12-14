package com.oheat.shop.fake;

import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.repository.MenuRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MemoryMenuRepository implements MenuRepository {

    private final Map<Long, MenuJpaEntity> menus = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(MenuJpaEntity menuJpaEntity) {
        menus.put(autoId++, menuJpaEntity);
    }

    @Override
    public Optional<MenuJpaEntity> findById(Long menuId) {
        return Optional.ofNullable(menus.get(menuId));
    }

    @Override
    public Optional<MenuJpaEntity> findByName(String name) {
        return menus.values().stream()
            .filter(menu -> menu.getName().equals(name))
            .findFirst();
    }

    public List<MenuJpaEntity> findAll() {
        return menus.values().stream().toList();
    }

    @Override
    public void deleteById(Long menuId) {
        menus.remove(menuId);
    }
}
