package com.oheat.food.repository;

import com.oheat.food.entity.OptionGroupJpaEntity;
import java.util.Optional;

public interface OptionGroupRepository {

    Optional<OptionGroupJpaEntity> findById(Long optionGroupId);
}
