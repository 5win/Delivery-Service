package com.oheat.food.dto;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionGroupSaveRequest {

    private final Long menuId;
    private final String name;
    private final boolean required;
    private final int maxNumOfSelect;

    public OptionGroupJpaEntity toEntity(MenuJpaEntity menu) {
        return OptionGroupJpaEntity.builder()
            .name(this.name)
            .required(this.required)
            .maxNumOfSelect(this.maxNumOfSelect)
            .menu(menu)
            .build();
    }
}
