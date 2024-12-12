package com.oheat.shop.repository;

import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuGroupMappingJpaRepository extends
    JpaRepository<MenuGroupMappingJpaEntity, Long> {

}
