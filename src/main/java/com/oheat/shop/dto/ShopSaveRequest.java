package com.oheat.shop.dto;

import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class ShopSaveRequest {

    private final String shopName;
    private final String phone;
    private final String categoryName;
    private final int minimumOrderAmount;

    public ShopJpaEntity toEntity() {
        return ShopJpaEntity.builder()
            .name(this.shopName)
            .phone(this.phone)
            .category(CategoryJpaEntity.builder()
                .name(categoryName)
                .build())
            .minimumOrderAmount(this.minimumOrderAmount)
            .build();
    }
}