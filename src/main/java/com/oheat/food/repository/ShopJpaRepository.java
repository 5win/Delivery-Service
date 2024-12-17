package com.oheat.food.repository;

import com.oheat.food.entity.ShopJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<ShopJpaEntity, Long>,
    ShopCustomRepository {

    Optional<ShopJpaEntity> findByName(String name);

    void deleteByName(String name);
}
