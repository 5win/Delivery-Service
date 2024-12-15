package com.oheat.food.dto;

import com.oheat.food.entity.MenuJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuFindByShopIdResponse {

    private final Long id;
    private final String name;
    private final int price;
    private final Long shopId;

    public static MenuFindByShopIdResponse from(MenuJpaEntity menu) {
        return MenuFindByShopIdResponse.builder()
            .id(menu.getId())
            .name(menu.getName())
            .price(menu.getPrice())
            .shopId(menu.getShop().getId())
            .build();
    }
}
