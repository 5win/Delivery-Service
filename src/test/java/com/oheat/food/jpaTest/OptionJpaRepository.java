package com.oheat.food.jpaTest;

import com.oheat.food.entity.OptionJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionJpaRepository extends JpaRepository<OptionJpaEntity, Long> {

}
