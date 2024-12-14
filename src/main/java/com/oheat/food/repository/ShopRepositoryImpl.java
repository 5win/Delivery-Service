package com.oheat.food.repository;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
    public List<ShopJpaEntity> findByCategory(CategoryJpaEntity category) {
        return shopJpaRepository.findByCategory(category);
    }

    @Override
    public void deleteByName(String name) {
        shopJpaRepository.deleteByName(name);
    }
}
