package com.oheat.food.service;

import com.oheat.food.dto.ShopSaveRequest;
import com.oheat.food.dto.ShopUpdateRequest;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.CategoryNotExistsException;
import com.oheat.food.exception.DuplicateShopNameException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.repository.CategoryRepository;
import com.oheat.food.repository.ShopRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;

    public void registerShop(ShopSaveRequest saveRequest) {

        shopRepository.findByName(saveRequest.getName())
            .ifPresent((shop) -> {
                throw new DuplicateShopNameException();
            });
        CategoryJpaEntity category = categoryRepository.findByName(
                saveRequest.getCategory())
            .orElseThrow(CategoryNotExistsException::new);

        shopRepository.save(saveRequest.toEntity(category));
    }

    public List<ShopJpaEntity> findShopByCategory(String categoryName) {
        CategoryJpaEntity category = categoryRepository.findByName(categoryName)
            .orElseThrow(CategoryNotExistsException::new);

        return shopRepository.findByCategory(category);
    }

    @Transactional
    public void updateShop(ShopUpdateRequest updateRequest) {
        ShopJpaEntity shop = shopRepository.findById(updateRequest.getId())
            .orElseThrow(ShopNotExistsException::new);
        CategoryJpaEntity category = categoryRepository.findByName(updateRequest.getCategory())
            .orElseThrow(CategoryNotExistsException::new);

        shop.updateShopInfo(updateRequest, category);
    }

    public void deleteShop(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);

        shopRepository.deleteById(shopId);
    }
}
