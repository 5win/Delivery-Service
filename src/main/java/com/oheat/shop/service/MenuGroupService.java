package com.oheat.shop.service;

import com.oheat.shop.dto.MenuGroupSaveRequest;
import com.oheat.shop.entity.MenuGroupJpaEntity;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.DuplicateMenuException;
import com.oheat.shop.exception.MenuNotExistsException;
import com.oheat.shop.exception.ShopNotExistsException;
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
    private final MenuGroupRepository menuGroupRepository;

    public void save(MenuGroupSaveRequest saveRequest) {
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        MenuGroupJpaEntity menuGroup = saveRequest.toEntity();

        saveRequest.getMenuList().forEach(menuId -> {
            MenuJpaEntity menu = menuRepository.findById(menuId)
                .orElseThrow(MenuNotExistsException::new);

            // Todo 개선 필요
            if (!shop.containsMenu(menu)) {
                throw new MenuNotExistsException();
            }
            if (menuGroup.containsMenu(menu)) {
                throw new DuplicateMenuException();
            }
            menuGroup.addMenu(menu);
        });

        menuGroupRepository.save(menuGroup);
    }
}
