package com.oheat.shop.dto;

import com.oheat.shop.entity.OptionGroupJpaEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionGroupSaveRequest {

    private final String name;
    private final Long menuId;
    private final boolean required;
    private final int maxNumOfSelect;

    public OptionGroupJpaEntity toEntity() {
        return OptionGroupJpaEntity.builder()
            .name(this.name)
            .menuId(this.menuId)
            .required(this.required)
            .maxNumOfSelect(this.maxNumOfSelect)
            .build();
    }
}
