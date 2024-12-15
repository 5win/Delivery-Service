package com.oheat.food.dto;

import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopSaveRequest {

    private final String name;
    private final String phone;
    private final String category;
    private final int minimumOrderAmount;

    public ShopJpaEntity toEntity(CategoryJpaEntity category) {
        return ShopJpaEntity.builder()
            .name(this.name)
            .phone(this.phone)
            .category(category)
            .minimumOrderAmount(this.minimumOrderAmount)
            .build();
    }
}
