package com.oheat.shop.service;

import com.oheat.shop.dto.MenuFindByShopIdResponse;
import com.oheat.shop.dto.MenuSaveRequest;
import com.oheat.shop.dto.MenuUpdateRequest;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.DuplicateMenuException;
import com.oheat.shop.exception.MenuNotExistsException;
import com.oheat.shop.exception.NoOptionException;
import com.oheat.shop.exception.NoOptionGroupException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    public void save(MenuSaveRequest saveRequest) {
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        menuRepository.findByName(saveRequest.getName())
            .ifPresent(menu -> {
                throw new DuplicateMenuException();
            });

        MenuJpaEntity menu = saveRequest.toEntity(shop);
        if (menu.isOptionGroupsEmpty()) {
            throw new NoOptionGroupException();
        }
        if (menu.isEmptyOptionGroupExists()) {
            throw new NoOptionException();
        }
        menuRepository.save(menu);
    }

    public List<MenuFindByShopIdResponse> findByShopId(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);

        return shop.getMenuSet().stream()
            .map(MenuFindByShopIdResponse::from)
            .toList();
    }

    @Transactional
    public void updateMenu(MenuUpdateRequest updateRequest) {
        ShopJpaEntity shop = shopRepository.findById(updateRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        MenuJpaEntity menu = menuRepository.findById(updateRequest.getMenuId())
            .orElseThrow(MenuNotExistsException::new);

        MenuJpaEntity updatedMenu = updateRequest.toEntity(shop);

        if (updatedMenu.isOptionGroupsEmpty()) {
            throw new NoOptionGroupException();
        }
        if (updatedMenu.isEmptyOptionGroupExists()) {
            throw new NoOptionException();
        }
        menu.updateMenu(updatedMenu);
    }

    public void deleteById(Long menuId) {
        menuRepository.findById(menuId)
            .orElseThrow(MenuNotExistsException::new);
        menuRepository.deleteById(menuId);
    }
}
