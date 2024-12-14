package com.oheat.shop.service;

import com.oheat.shop.dto.MenuGroupSaveRequest;
import com.oheat.shop.dto.MenuGroupUpdateRequest;
import com.oheat.shop.dto.MenuSaveToMenuGroupRequest;
import com.oheat.shop.entity.MenuGroupJpaEntity;
import com.oheat.shop.entity.MenuGroupMappingJpaEntity;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.exception.MenuGroupNotExistsException;
import com.oheat.shop.exception.MenuNotExistsException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.repository.MenuGroupMappingRepository;
import com.oheat.shop.repository.MenuGroupRepository;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenuGroupService {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final MenuGroupMappingRepository menuGroupMappingRepository;
    private final MenuGroupRepository menuGroupRepository;

    // 메뉴 그룹 CRUD
    public void registerMenuGroup(MenuGroupSaveRequest saveRequest) {
        shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);

        menuGroupRepository.save(saveRequest.toEntity());
    }

    @Transactional
    public void updateMenuGroup(MenuGroupUpdateRequest updateRequest) {
        MenuGroupJpaEntity menuGroup = menuGroupRepository.findById(updateRequest.getMenuGroupId())
            .orElseThrow(MenuGroupNotExistsException::new);

        menuGroup.updateMenuGroup(updateRequest);
    }

    public void deleteMenuGroup(Long menuGroupId) {
        menuGroupRepository.findById(menuGroupId)
            .orElseThrow(MenuGroupNotExistsException::new);

        menuGroupRepository.deleteById(menuGroupId);
    }

    // 메뉴 그룹 매핑 CRUD
    public void registerMenuToMenuGroup(MenuSaveToMenuGroupRequest saveRequest) {
        MenuGroupJpaEntity menuGroup = menuGroupRepository.findById(saveRequest.getMenuGroupId())
            .orElseThrow(MenuGroupNotExistsException::new);

        MenuJpaEntity menu = menuRepository.findById(saveRequest.getMenuId())
            .orElseThrow(MenuNotExistsException::new);

        MenuGroupMappingJpaEntity menuGroupMapping = MenuGroupMappingJpaEntity.builder()
            .menuGroup(menuGroup)
            .menu(menu)
            .build();

        menuGroup.addMenuMapping(menuGroupMapping);
        menuGroupMappingRepository.save(menuGroupMapping);
    }

    public void deleteMenuFromMenuGroup(Long menuGroupMappingId) {
        menuGroupMappingRepository.findById(menuGroupMappingId)
            .orElseThrow(MenuNotExistsException::new);

        menuGroupMappingRepository.deleteById(menuGroupMappingId);
    }
}
