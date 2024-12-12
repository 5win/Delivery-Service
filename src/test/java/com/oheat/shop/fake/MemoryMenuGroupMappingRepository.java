package com.oheat.shop.fake;

import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import com.oheat.shop.repository.MenuGroupMappingRepository;
import java.util.HashMap;
import java.util.Map;

public class MemoryMenuGroupMappingRepository implements MenuGroupMappingRepository {

    private final Map<Long, MenuGroupMappingJpaEntity> menuGroupMappings = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(MenuGroupMappingJpaEntity menuGroupMapping) {
        menuGroupMappings.put(autoId++, menuGroupMapping);
    }
}
