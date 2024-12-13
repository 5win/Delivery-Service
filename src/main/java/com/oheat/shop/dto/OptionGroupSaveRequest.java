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
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .name(this.name)
            .required(this.required)
            .maxNumOfSelect(this.maxNumOfSelect)
            .build();

        options.forEach(optionSaveRequest -> {
            optionGroup.addOption(optionSaveRequest.toEntity());
        });

        return optionGroup;
    }
}
