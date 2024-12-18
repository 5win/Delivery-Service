package com.oheat.food.fake;

import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.repository.OptionGroupRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryOptionGroupRepository implements OptionGroupRepository {

    private final Map<Long, OptionGroupJpaEntity> options = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public Optional<OptionGroupJpaEntity> findById(Long optionGroupId) {
        return Optional.ofNullable(options.get(optionGroupId));
    }
}
