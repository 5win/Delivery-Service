package com.oheat.food.repository;

import com.oheat.food.entity.MenuJpaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuJpaRepository extends JpaRepository<MenuJpaEntity, Long> {

    Optional<MenuJpaEntity> findByName(String name);
}
