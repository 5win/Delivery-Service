package com.oheat.food.dto;

import com.oheat.food.entity.MenuGroupJpaEntity;
import com.oheat.food.entity.MenuJpaEntity;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@RequiredArgsConstructor
public class MenuGroupFindResponse {

    private final Long id;
    private final String name;
    private final List<MenuResponse> menuList;

    public static MenuGroupFindResponse from(MenuGroupJpaEntity menuGroup) {
        List<MenuResponse> menuList = menuGroup.getMenuGroupMappingSet().stream()
            .map(mapping -> MenuResponse.from(mapping.getMenu()))
            .toList();

        return MenuGroupFindResponse.builder()
            .id(menuGroup.getId())
            .name(menuGroup.getName())
            .menuList(menuList)
            .build();
    }

    @Builder
    @Getter
    @RequiredArgsConstructor
    private static class MenuResponse {

        private final Long id;
        private final String name;
        private final int price;

        public static MenuResponse from(MenuJpaEntity menu) {
            return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
        }
    }
}
