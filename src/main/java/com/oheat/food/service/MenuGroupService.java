package com.oheat.food.service;

import com.oheat.food.dto.MenuGroupSaveRequest;
import com.oheat.food.dto.MenuGroupUpdateRequest;
import com.oheat.food.dto.MenuSaveToMenuGroupRequest;
import com.oheat.food.entity.MenuGroupJpaEntity;
import com.oheat.food.entity.MenuGroupMappingJpaEntity;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.MenuGroupNotExistsException;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.repository.MenuGroupMappingRepository;
import com.oheat.food.repository.MenuGroupRepository;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.ShopRepository;
import java.util.List;
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
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);

        menuGroupRepository.save(saveRequest.toEntity(shop));
    }

    public List<MenuGroupJpaEntity> findMenuGroupByShopId(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);
        return shop.getMenuGroups();
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
