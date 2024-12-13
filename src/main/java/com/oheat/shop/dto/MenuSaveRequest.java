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
    private final int price;
    private final Long shopId;
    private final List<OptionGroupSaveRequest> optionGroups;

    public MenuJpaEntity toEntity() {
        MenuJpaEntity menu = MenuJpaEntity.builder()
            .name(this.name)
            .price(this.price)
            .shopId(this.shopId)
            .build();

        optionGroups.forEach(optionGroupSaveRequest -> {
            menu.addOptionGroup(optionGroupSaveRequest.toEntity());
        });

        return menu;
    }
}
