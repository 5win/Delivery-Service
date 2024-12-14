package com.oheat.shop.dto;

import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuUpdateRequest {

    private final Long menuId;
    private final String name;
    private final int price;
    private final Long shopId;
    private final List<OptionGroupUpdateRequest> optionGroups;

    public MenuJpaEntity toEntity(ShopJpaEntity shop) {
        MenuJpaEntity menu = MenuJpaEntity.builder()
            .name(this.name)
            .price(this.price)
            .shop(shop)
            .build();

        this.optionGroups.forEach(optionGroupUpdateRequest -> {
            menu.addOptionGroup(optionGroupUpdateRequest.toEntity());
        });

        return menu;
    }
}
