package com.oheat.food.service;

import com.oheat.food.dto.MenuSaveRequest;
import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.NoOptionException;
import com.oheat.food.exception.NoOptionGroupException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.ShopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    public void registerMenu(MenuSaveRequest saveRequest) {
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);

        MenuJpaEntity menu = saveRequest.toEntity(shop);
        shop.addMenu(menu);
        if (menu.isOptionGroupsEmpty()) {
            throw new NoOptionGroupException();
        }
        if (menu.isEmptyOptionGroupExists()) {
            throw new NoOptionException();
        }
        menuRepository.save(menu);
    }

    public List<MenuJpaEntity> findByShopId(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);

        return shop.getMenuSet().stream().toList();
    }

    @Transactional
    public void updateMenu(MenuUpdateRequest updateRequest) {
        shopRepository.findById(updateRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        MenuJpaEntity menu = menuRepository.findById(updateRequest.getMenuId())
            .orElseThrow(MenuNotExistsException::new);

        menu.updateMenu(updateRequest);
    }

    public void deleteById(Long menuId) {
        menuRepository.findById(menuId)
            .orElseThrow(MenuNotExistsException::new);
        menuRepository.deleteById(menuId);
    }
}
