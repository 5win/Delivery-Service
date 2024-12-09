package com.oheat.shop.dto;

import com.oheat.shop.entity.OptionGroupJpaEntity;
import java.util.List;
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
    private final List<OptionSaveRequest> options;

    public OptionGroupJpaEntity toEntity() {
        return OptionGroupJpaEntity.builder()
            .name(this.name)
            .menuId(this.menuId)
            .required(this.required)
            .maxNumOfSelect(this.maxNumOfSelect)
            .options(this.options.stream()
                .map(OptionSaveRequest::toEntity)
                .toList())
            .build();
    }
}
