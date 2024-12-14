package com.oheat.food.repository;

import com.oheat.food.entity.MenuGroupMappingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupMappingJpaRepository extends
    JpaRepository<MenuGroupMappingJpaEntity, Long> {

}
