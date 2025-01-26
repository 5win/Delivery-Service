package com.oheat.food.service;

import com.oheat.common.sido.Sido;
import com.oheat.common.sido.SidoNotExistsException;
import com.oheat.common.sido.SidoRepository;
import com.oheat.common.sigungu.Sigungu;
import com.oheat.common.sigungu.SigunguNotExistsException;
import com.oheat.common.sigungu.SigunguRepository;
import com.oheat.food.dto.Coordinates;
import com.oheat.food.dto.ShopFindRequest;
import com.oheat.food.dto.ShopFindResponse;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShopService {

    private final ShopRepository shopRepository;
    private final CategoryRepository categoryRepository;
    private final SidoRepository sidoRepository;
    private final SigunguRepository sigunguRepository;

    public void registerShop(ShopSaveRequest saveRequest) {

        shopRepository.findByName(saveRequest.getName())
            .ifPresent((shop) -> {
                throw new DuplicateShopNameException();
            });
        CategoryJpaEntity category = categoryRepository.findByName(saveRequest.getCategory())
            .orElseThrow(CategoryNotExistsException::new);

        Sido sido = sidoRepository.findByCtpKorNm(saveRequest.getSido())
            .orElseThrow(() -> new SidoNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 시도 지역입니다."));
        Sigungu sigungu = sigunguRepository.findBySigKorNm(saveRequest.getSigungu())
            .orElseThrow(() -> new SigunguNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 시군구 지역입니다."));

        shopRepository.save(saveRequest.toEntity(category, sido, sigungu));
    }

    public Page<ShopFindResponse> findShopByCategory(ShopFindRequest findRequest, Pageable pageable) {
        CategoryJpaEntity category = categoryRepository.findByName(findRequest.getCategoryName())
            .orElseThrow(CategoryNotExistsException::new);

        Order distanceOrder = pageable.getSort().getOrderFor("distance");
        if (distanceOrder == null) {
            return shopRepository.findByCategory(category, pageable)
                .map(ShopFindResponse::from);
        }
        return shopRepository.findByCategoryOrderByDistance(category, Coordinates.from(findRequest), pageable)
            .map(ShopFindResponse::from);
    }

    @Transactional
    public void updateShop(ShopUpdateRequest updateRequest) {
        ShopJpaEntity shop = shopRepository.findById(updateRequest.getId())
            .orElseThrow(ShopNotExistsException::new);
        CategoryJpaEntity category = categoryRepository.findByName(updateRequest.getCategory())
            .orElseThrow(CategoryNotExistsException::new);

        Sido sido = sidoRepository.findByCtpKorNm(updateRequest.getSido())
            .orElseThrow(() -> new SidoNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 시도 지역입니다."));
        Sigungu sigungu = sigunguRepository.findBySigKorNm(updateRequest.getSigungu())
            .orElseThrow(() -> new SigunguNotExistsException(HttpStatus.BAD_REQUEST, "존재하지 않는 시군구 지역입니다."));

        shop.updateShopInfo(updateRequest, category, sido, sigungu);
    }

    public void deleteShop(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);

        shopRepository.deleteById(shopId);
    }
}
