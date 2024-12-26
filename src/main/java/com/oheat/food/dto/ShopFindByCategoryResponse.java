package com.oheat.food.dto;

import com.oheat.food.entity.ShopJpaEntity;
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
    private final int deliveryFee;

    public static ShopFindByCategoryResponse from(ShopJpaEntity shopJpaEntity) {
        return ShopFindByCategoryResponse.builder()
            .id(shopJpaEntity.getId())
            .name(shopJpaEntity.getName())
            .phone(shopJpaEntity.getPhone())
            .category(shopJpaEntity.getCategory().getName())
            .minimumOrderAmount(shopJpaEntity.getMinimumOrderAmount())
            .deliveryFee((shopJpaEntity.getDeliveryFee()))
            .build();
    }
}
