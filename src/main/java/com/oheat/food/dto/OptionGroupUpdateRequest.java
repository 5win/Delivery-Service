package com.oheat.food.dto;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class OptionGroupUpdateRequest {

    private final String name;
    private final boolean required;
    private final int maxNumOfSelect;
    private final List<OptionUpdateRequest> options;

    public OptionGroupJpaEntity toEntity(MenuJpaEntity menu) {
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder()
            .name(this.name)
            .required(this.required)
            .maxNumOfSelect(this.maxNumOfSelect)
            .menu(menu)
            .build();

        options.forEach(optionSaveRequest -> {
            optionGroup.addOption(optionSaveRequest.toEntity(optionGroup));
        });

        return optionGroup;
    }
}
