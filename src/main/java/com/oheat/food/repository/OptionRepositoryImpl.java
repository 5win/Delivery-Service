package com.oheat.food.repository;

import com.oheat.food.entity.OptionJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OptionRepositoryImpl implements OptionRepository {

    private final OptionJpaRepository optionJpaRepository;

    @Override
    public void save(OptionJpaEntity option) {
        optionJpaRepository.save(option);
    }

    @Override
    public Optional<OptionJpaEntity> findById(Long optionId) {
        return optionJpaRepository.findById(optionId);
    }

    @Override
    public void delete(OptionJpaEntity option) {
        optionJpaRepository.delete(option);
    }
}
