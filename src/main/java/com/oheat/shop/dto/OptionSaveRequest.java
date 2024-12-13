package com.oheat.shop.dto;

import com.oheat.shop.entity.OptionJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionSaveRequest {

    private final String name;
    private final int price;
    private final Long optionGroupId;

    public OptionJpaEntity toEntity() {
        return OptionJpaEntity.builder()
            .name(this.name)
            .price(this.price)
            .build();
    }
}
