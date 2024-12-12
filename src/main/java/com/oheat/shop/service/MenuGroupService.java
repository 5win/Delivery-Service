package com.oheat.shop.service;

import com.oheat.shop.dto.MenuGroupSaveRequest;
import com.oheat.shop.dto.MenuSaveToMenuGroupRequest;
import com.oheat.shop.entity.MenuGroupJpaEntity;
import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.exception.DuplicateMenuException;
import com.oheat.shop.exception.MenuGroupNotExistsException;
import com.oheat.shop.exception.MenuNotExistsException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.repository.MenuGroupMappingRepository;
import com.oheat.shop.repository.MenuGroupRepository;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuGroupService {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupMappingRepository menuGroupMappingRepository;
    private final MenuGroupRepository menuGroupRepository;

    public void registerMenuGroup(MenuGroupSaveRequest saveRequest) {
        shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);

        menuGroupRepository.save(saveRequest.toEntity());
    }

    public void registerMenuToMenuGroup(MenuSaveToMenuGroupRequest saveRequest) {
        MenuGroupJpaEntity menuGroup = menuGroupRepository.findById(
                saveRequest.getMenuGroupId())
            .orElseThrow(MenuGroupNotExistsException::new);

        saveRequest.getMenuList().forEach(menuId -> {
            MenuJpaEntity menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotExistsException::new);

            MenuGroupMappingJpaEntity menuGroupMapping = MenuGroupMappingJpaEntity.builder()
                .menuGroup(menuGroup)
                .menu(menu)
                .build();

            if (menuGroup.containsMenu(menuGroupMapping)) {
                throw new DuplicateMenuException();
            }
            menuGroup.addMenuMapping(menuGroupMapping);
            menuGroupMappingRepository.save(menuGroupMapping);
        });
    }
}
