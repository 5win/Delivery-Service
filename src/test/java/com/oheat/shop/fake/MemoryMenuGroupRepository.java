package com.oheat.shop.fake;

import com.oheat.shop.entity.MenuGroupJpaEntity;
import com.oheat.shop.repository.MenuGroupRepository;
import java.util.HashMap;
import java.util.Map;

public class MemoryMenuGroupRepository implements MenuGroupRepository {

    private final Map<Long, MenuGroupJpaEntity> menuGroups = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(MenuGroupJpaEntity menuGroup) {
        menuGroups.put(autoId++, menuGroup);
    }
}
