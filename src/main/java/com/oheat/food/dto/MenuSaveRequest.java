package com.oheat.food.dto;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuSaveRequest {

    private final String name;
    private final int price;
    private final Long shopId;

    public MenuJpaEntity toEntity(ShopJpaEntity shop) {
        return MenuJpaEntity.builder()
            .name(this.name)
            .price(this.price)
            .shop(shop)
            .build();
    }
}
