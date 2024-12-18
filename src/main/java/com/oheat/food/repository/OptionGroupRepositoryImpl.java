package com.oheat.food.repository;

import com.oheat.food.entity.OptionGroupJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OptionGroupRepositoryImpl implements OptionGroupRepository {

    private final OptionGroupJpaRepository optionGroupJpaRepository;

    @Override
    public Optional<OptionGroupJpaEntity> findById(Long optionGroupId) {
        return optionGroupJpaRepository.findById(optionGroupId);
    }
}
