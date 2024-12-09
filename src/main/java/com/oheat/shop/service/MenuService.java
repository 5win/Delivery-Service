package com.oheat.shop.service;

import com.oheat.shop.dto.MenuFindByShopIdResponse;
import com.oheat.shop.dto.MenuSaveRequest;
import com.oheat.shop.entity.MenuJpaEntity;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.exception.NoOptionGroupException;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final ShopRepository shopRepository;

    public void save(MenuSaveRequest saveRequest) {
        shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);

        MenuJpaEntity menu = saveRequest.toEntity();
        if (menu.isOptionGroupsEmpty()) {
            throw new NoOptionGroupException();
        }
        menuRepository.save(menu);
    }

    public List<MenuFindByShopIdResponse> findByShopId(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);

        return shop.getMenuList().stream()
            .map(MenuFindByShopIdResponse::from)
            .toList();
    }
}
