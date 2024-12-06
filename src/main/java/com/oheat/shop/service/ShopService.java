package com.oheat.shop.service;

import com.oheat.shop.dto.ShopSaveRequest;
import com.oheat.shop.exception.CategoryNotExistsException;
import com.oheat.shop.exception.DuplicateShopNameException;
import com.oheat.shop.repository.CategoryRepository;
import com.oheat.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;

    public void registerShop(ShopSaveRequest saveRequest) {

        shopRepository.findByName(saveRequest.getShopName())
            .ifPresent((shop) -> {
                throw new DuplicateShopNameException();
            });
        categoryRepository.findByName(saveRequest.getCategoryName())
            .orElseThrow(CategoryNotExistsException::new);

        shopRepository.save(saveRequest.toEntity());
    }
}
