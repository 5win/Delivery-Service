package com.oheat.shop.repository;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShopJpaRepository extends JpaRepository<ShopJpaEntity, Long> {

    Optional<ShopJpaEntity> findByName(String name);

    List<ShopJpaEntity> findByCategory(CategoryJpaEntity category);

    void deleteByName(String name);
}
