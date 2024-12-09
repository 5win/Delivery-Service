package com.oheat.shop.dto;

import com.oheat.shop.entity.MenuJpaEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuSaveRequest {

    private final String name;
    private final Long shopId;
    private final List<OptionGroupSaveRequest> optionGroups;

    public MenuJpaEntity toEntity() {
        return MenuJpaEntity.builder()
            .name(this.name)
            .shopId(this.shopId)
            .optionGroups(this.optionGroups.stream()
                .map(OptionGroupSaveRequest::toEntity)
                .toList())
            .build();
    }
}
