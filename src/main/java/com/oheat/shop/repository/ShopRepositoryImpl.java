package com.oheat.shop.repository;

import com.oheat.shop.entity.ShopJpaEntity;
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
    public Optional<ShopJpaEntity> findByName(String name) {
        return shopJpaRepository.findByName(name);
    }
}
