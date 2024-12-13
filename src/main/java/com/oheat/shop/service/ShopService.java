package com.oheat.shop.service;

import com.oheat.shop.dto.ShopFindByCategoryResponse;
import com.oheat.shop.dto.ShopSaveRequest;
import com.oheat.shop.entity.CategoryJpaEntity;
import com.oheat.shop.exception.CategoryNotExistsException;
import com.oheat.shop.exception.DuplicateShopNameException;
import com.oheat.shop.repository.CategoryRepository;
import com.oheat.shop.repository.ShopRepository;
import java.util.List;
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
        CategoryJpaEntity category = categoryRepository.findByName(
                saveRequest.getCategoryName())
            .orElseThrow(CategoryNotExistsException::new);

        shopRepository.save(saveRequest.toEntity(category));
    }

    public List<ShopFindByCategoryResponse> findShopByCategory(String categoryName) {
        CategoryJpaEntity category = categoryRepository.findByName(categoryName)
            .orElseThrow(CategoryNotExistsException::new);

        return shopRepository.findByCategory(category).stream()
            .map(ShopFindByCategoryResponse::from)
            .toList();
    }
}
