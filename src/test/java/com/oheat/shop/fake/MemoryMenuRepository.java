package com.oheat.shop.fake;

import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.repository.MenuRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryMenuRepository implements MenuRepository {

    private final Map<Long, MenuJpaEntity> menus = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(MenuJpaEntity menuJpaEntity) {
        menus.put(autoId++, menuJpaEntity);
    }

    public List<MenuJpaEntity> findAll() {
        return menus.values().stream().toList();
    }
}
