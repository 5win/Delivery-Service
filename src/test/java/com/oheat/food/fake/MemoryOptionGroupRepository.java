package com.oheat.food.fake;

import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.repository.OptionGroupRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryOptionGroupRepository implements OptionGroupRepository {

    private final Map<Long, OptionGroupJpaEntity> groups = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(OptionGroupJpaEntity optionGroup) {
        groups.put(autoId++, optionGroup);
    }

    @Override
    public Optional<OptionGroupJpaEntity> findById(Long optionGroupId) {
        return Optional.ofNullable(groups.get(optionGroupId));
    }

    @Override
    public void delete(OptionGroupJpaEntity optionGroup) {
        Long target = groups.entrySet().stream()
            .filter(entry -> entry.getValue().equals(optionGroup))
            .findFirst()
            .get().getKey();

        groups.remove(target);
    }
}
