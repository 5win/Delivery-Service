package com.oheat.shop.dto;

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
    private final int minimumOrderAmount;

    public ShopJpaEntity toEntity() {
        return ShopJpaEntity.builder()
            .shopName(this.shopName)
            .phone(this.phone)
            .minimumOrderAmount(this.minimumOrderAmount)
            .build();
    }
}
