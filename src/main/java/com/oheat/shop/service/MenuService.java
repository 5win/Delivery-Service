package com.oheat.shop.service;

import com.oheat.shop.dto.MenuSaveRequest;
import com.oheat.shop.exception.ShopNotExistsException;
import com.oheat.shop.repository.MenuRepository;
import com.oheat.shop.repository.ShopRepository;
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

        menuRepository.save(saveRequest.toEntity());
    }
}
