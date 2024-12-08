package com.oheat.shop.dto;

import com.oheat.shop.entity.MenuJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuSaveRequest {

    private final String name;
    private final Long shopId;

    public MenuJpaEntity toEntity() {
        return MenuJpaEntity.builder()
            .name(this.name)
            .shopId(this.shopId)
            .build();
    }
}
