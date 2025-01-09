package com.oheat.food.fake;

import com.oheat.food.dto.Coordinates;
import com.oheat.food.entity.CategoryJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.repository.ShopRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@Getter
public class MemoryShopRepository implements ShopRepository {

    private final Map<Long, ShopJpaEntity> shops = new HashMap<>();
    private Long autoId = 1L;

    @Override
    public void save(ShopJpaEntity shopJpaEntity) {
        shops.put(autoId++, shopJpaEntity);
    }

    @Override
    public Optional<ShopJpaEntity> findById(Long id) {
        return Optional.ofNullable(shops.get(id));
    }

    @Override
    public Optional<ShopJpaEntity> findByName(String name) {
        return shops.values().stream()
            .filter(shop -> shop.getName().equals(name))
            .findFirst();
    }

    @Override
    public Page<ShopJpaEntity> findByCategory(CategoryJpaEntity category, Pageable pageable) {
        List<ShopJpaEntity> shopList = shops.values().stream()
            .filter(shop -> shop.getCategory().equals(category))
            .toList();
        return new PageImpl<>(shopList);
    }

    @Override
    public Page<ShopJpaEntity> findByCategoryOrderByDistance(CategoryJpaEntity category, Coordinates coordinates,
        Pageable pageable) {
        // Do nothing
        return null;
    }

    @Override
    public void deleteById(Long shopId) {
        shops.remove(shopId);
    }
}
