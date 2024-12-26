package com.oheat.food.repository;

import com.oheat.food.entity.OptionGroupJpaEntity;
import java.util.Optional;

public interface OptionGroupRepository {

    void save(OptionGroupJpaEntity optionGroup);

    Optional<OptionGroupJpaEntity> findById(Long optionGroupId);

    void delete(OptionGroupJpaEntity optionGroup);
}
