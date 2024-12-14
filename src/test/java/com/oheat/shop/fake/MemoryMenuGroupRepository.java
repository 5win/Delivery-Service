package com.oheat.shop.fake;

import com.oheat.shop.entity.MenuGroupJpaEntity;
import com.oheat.shop.repository.MenuGroupRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryMenuGroupRepository implements MenuGroupRepository {

    private final Map<Long, MenuGroupJpaEntity> menuGroups = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(MenuGroupJpaEntity menuGroup) {
        menuGroups.put(autoId++, menuGroup);
    }

    @Override
    public Optional<MenuGroupJpaEntity> findById(Long menuGroupId) {
        return Optional.ofNullable(menuGroups.get(menuGroupId));
    }

    @Override
    public void deleteById(Long menuGroupId) {
        menuGroups.remove(menuGroupId);
    }
}
