package com.oheat.food.dto;

import com.oheat.food.entity.CategoryJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CategorySaveRequest {

    private final String name;

    public CategoryJpaEntity toEntity() {
        return CategoryJpaEntity.builder().name(this.name).build();
    }
}
