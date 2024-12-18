package com.oheat.food.fake;

import com.oheat.food.entity.MenuGroupJpaEntity;
import com.oheat.food.repository.MenuGroupRepository;
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
