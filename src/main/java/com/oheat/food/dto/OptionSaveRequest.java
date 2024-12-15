package com.oheat.food.dto;

import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionSaveRequest {

    private final String name;
    private final int price;

    public OptionJpaEntity toEntity(OptionGroupJpaEntity optionGroup) {
        return OptionJpaEntity.builder()
            .name(this.name)
            .price(this.price)
            .optionGroup(optionGroup)
            .build();
    }
}
