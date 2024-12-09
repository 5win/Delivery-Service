package com.oheat.shop.dto;

import com.oheat.shop.entity.MenuJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuFindByShopIdResponse {

    private final Long id;
    private final String name;
    private final Long shopId;

    public static MenuFindByShopIdResponse from(MenuJpaEntity entity) {
        return MenuFindByShopIdResponse.builder()
            .id(entity.getId())
            .name(entity.getName())
            .shopId(entity.getShopId())
            .build();
    }
}
