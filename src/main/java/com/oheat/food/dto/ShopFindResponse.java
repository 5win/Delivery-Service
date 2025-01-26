package com.oheat.food.dto;

import com.oheat.food.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopFindResponse {

    private final Long id;
    private final String name;
    private final String phone;
    private final String category;
    private final int minimumOrderAmount;
    private final int deliveryFee;
    private final String address;
    private final Double latitude;
    private final Double longitude;

    public static ShopFindResponse from(ShopJpaEntity shopJpaEntity) {
        return ShopFindResponse.builder()
            .id(shopJpaEntity.getId())
            .name(shopJpaEntity.getName())
            .phone(shopJpaEntity.getPhone())
            .category(shopJpaEntity.getCategory().getName())
            .minimumOrderAmount(shopJpaEntity.getMinimumOrderAmount())
            .deliveryFee((shopJpaEntity.getDeliveryFee()))
            .address(shopJpaEntity.getAddress())
            .latitude(shopJpaEntity.getLatitude())
            .longitude(shopJpaEntity.getLongitude())
            .build();
    }
}
