package com.oheat.shop.service;

import com.oheat.shop.dto.ShopSaveRequest;
import com.oheat.shop.exception.DuplicateShopNameException;
import com.oheat.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public void registerShop(ShopSaveRequest saveRequest) {

        shopRepository.findByName(saveRequest.getShopName())
            .ifPresent((shop) -> {
                throw new DuplicateShopNameException();
            });

        shopRepository.save(saveRequest.toEntity());
    }
}
