package com.oheat.shop.dto;

import com.oheat.shop.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopFindAllResponse {

    private final Long id;
    private final String name;
    private final String phone;
    private final String category;
    private final int minimumOrderAmount;

    public static ShopFindAllResponse from(ShopJpaEntity shopJpaEntity) {
        return ShopFindAllResponse.builder()
            .id(shopJpaEntity.getId())
            .name(shopJpaEntity.getName())
            .phone(shopJpaEntity.getPhone())
            .category(shopJpaEntity.getCategory().getName())
            .minimumOrderAmount(shopJpaEntity.getMinimumOrderAmount())
            .build();
    }
}
