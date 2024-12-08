package com.oheat.shop;

import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.repository.MenuRepository;
import java.util.HashMap;
import java.util.Map;

public class MemoryMenuRepository implements MenuRepository {

    private final Map<Long, MenuJpaEntity> menus = new HashMap<>();

    @Override
    public void save(MenuJpaEntity menuJpaEntity) {
        menus.put(menuJpaEntity.getId(), menuJpaEntity);
    }
}
