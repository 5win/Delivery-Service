package com.oheat.shop.fake;

import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import com.oheat.shop.repository.MenuGroupMappingRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryMenuGroupMappingRepository implements MenuGroupMappingRepository {

    private final Map<Long, MenuGroupMappingJpaEntity> menuGroupMappings = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(MenuGroupMappingJpaEntity menuGroupMapping) {
        menuGroupMappings.put(autoId++, menuGroupMapping);
    }

    @Override
    public Optional<MenuGroupMappingJpaEntity> findById(Long menuGroupMappingId) {
        return Optional.ofNullable(menuGroupMappings.get(menuGroupMappingId));
    }

    @Override
    public void deleteById(Long menuGroupMappingId) {
        menuGroupMappings.remove(menuGroupMappingId);
    }
}
