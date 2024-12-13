package com.oheat.shop.dto;

import com.oheat.shop.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopFindByCategoryResponse {

    private final Long id;
    private final String name;
    private final String phone;
    private final String category;
    private final int minimumOrderAmount;

    public static ShopFindByCategoryResponse from(ShopJpaEntity shopJpaEntity) {
        return ShopFindByCategoryResponse.builder()
            .id(shopJpaEntity.getId())
            .name(shopJpaEntity.getName())
            .phone(shopJpaEntity.getPhone())
            .category(shopJpaEntity.getCategory().getName())
            .minimumOrderAmount(shopJpaEntity.getMinimumOrderAmount())
            .build();
    }
}
