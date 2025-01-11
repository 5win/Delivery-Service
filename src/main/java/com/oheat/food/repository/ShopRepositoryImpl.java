package com.oheat.food.repository;

import com.oheat.food.dto.Coordinates;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ShopRepositoryImpl implements ShopRepository {

    private final ShopJpaRepository shopJpaRepository;

    @Override
    public void save(ShopJpaEntity shopJpaEntity) {
        shopJpaRepository.save(shopJpaEntity);
    }

    @Override
    public Optional<ShopJpaEntity> findById(Long id) {
        return shopJpaRepository.findById(id);
    }

    @Override
    public Optional<ShopJpaEntity> findByName(String name) {
        return shopJpaRepository.findByName(name);
    }

    @Override
    public Page<ShopJpaEntity> findByCategory(CategoryJpaEntity category, Pageable pageable) {
        return shopJpaRepository.findByCategory(category, pageable);
    }

    @Override
    public Page<ShopJpaEntity> findByCategoryOrderByDistance(CategoryJpaEntity category, Coordinates coordinates,
        Pageable pageable) {
        return shopJpaRepository.findByCategoryOrderByDistance(category, coordinates, pageable);
    }

    @Override
    public void deleteById(Long shopId) {
        shopJpaRepository.deleteById(shopId);
    }
}
