package com.oheat.shop.dto;

import com.oheat.shop.entity.MenuGroupJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuGroupSaveRequest {

    private final String name;
    private final Long shopId;

    public MenuGroupJpaEntity toEntity() {
        return MenuGroupJpaEntity.builder()
            .name(this.name)
            .build();
    }
}