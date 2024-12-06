package com.oheat.shop;

import com.oheat.shop.dto.ShopSaveRequest;
import com.oheat.shop.entity.ShopJpaEntity;
import com.oheat.shop.service.ShopService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ShopServiceTest {

    private ShopService shopService;
    private final MemoryShopRepository memoryShopRepository = new MemoryShopRepository();

    @BeforeEach
    void setUp() {
        shopService = new ShopService(memoryShopRepository);
    }

    @Test
    @DisplayName("매장 이름이 중복되면 매장 등록 실패")
    void shopNameDuplicate_thenFail() {
        memoryShopRepository.save(
            ShopJpaEntity.builder()
                .shopName("오잇")
                .build());

        Assertions.assertThatThrownBy(() ->
            shopService.registerShop(
                ShopSaveRequest.builder()
                    .shopName("오잇")
                    .build()));
    }

    @Disabled
    @Test
    @DisplayName("카테고리가 없으면 매장 등록 실패")
    void whenCategoryNotExists_thenFail() {

    }

    @Disabled
    @Test
    @DisplayName("매장 이름에 숫자, 한글, 영어 이외의 글자가 포함되면 실패")
    void shopNameContainsOnlyNumberKoreanEnglish() {

    }

}
