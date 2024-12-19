package com.oheat.food.fake;

import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.repository.OptionRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MemoryOptionRepository implements OptionRepository {

    private final Map<Long, OptionJpaEntity> options = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(OptionJpaEntity option) {
        options.put(autoId++, option);
    }

    @Override
    public Optional<OptionJpaEntity> findById(Long optionId) {
        return Optional.ofNullable(options.get(optionId));
    }

    @Override
    public void delete(OptionJpaEntity option) {
        Long target = options.entrySet().stream()
            .filter(entry -> entry.getValue().equals(option))
            .findFirst()
            .get().getKey();
        options.remove(target);
    }
}
