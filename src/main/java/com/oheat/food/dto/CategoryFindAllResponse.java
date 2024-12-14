package com.oheat.food.dto;

import com.oheat.food.entity.CategoryJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class CategoryFindAllResponse {

    private final String name;

    public static CategoryFindAllResponse from(CategoryJpaEntity category) {
        return CategoryFindAllResponse.builder()
            .name(category.getName())
            .build();
    }
}
