package com.oheat.food.repository;

import com.oheat.food.entity.OptionJpaEntity;
import java.util.Optional;

public interface OptionRepository {

    void save(OptionJpaEntity option);

    Optional<OptionJpaEntity> findById(Long optionId);

    void delete(OptionJpaEntity option);
}
