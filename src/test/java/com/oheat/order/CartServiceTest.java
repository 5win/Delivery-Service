package com.oheat.order;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.OptionGroupNotExistsException;
import com.oheat.food.exception.OptionNotExistsException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.fake.MemoryMenuRepository;
import com.oheat.food.fake.MemoryOptionGroupRepository;
import com.oheat.food.fake.MemoryOptionRepository;
import com.oheat.food.fake.MemoryShopRepository;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.OptionGroupRepository;
import com.oheat.food.repository.OptionRepository;
import com.oheat.food.repository.ShopRepository;
import com.oheat.order.dto.CartSaveRequest;
import com.oheat.order.dto.CartSaveRequest.CartOptionGroupSaveRequest;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.order.exception.OtherShopMenuAlreadyExistsException;
import com.oheat.order.service.CartService;
import com.oheat.user.MemoryUserRepository;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartServiceTest {

    private final UserRepository memoryUserRepository = new MemoryUserRepository();
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final MenuRepository memoryMenuRepository = new MemoryMenuRepository();
    private final OptionGroupRepository memoryOptionGroupRepository = new MemoryOptionGroupRepository();
    private final OptionRepository memoryOptionRepository = new MemoryOptionRepository();
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(memoryUserRepository, memoryShopRepository,
            memoryMenuRepository, memoryOptionGroupRepository, memoryOptionRepository);
    }

    // Create Test
    @Test
    @DisplayName("장바구니에 담을 떄 사용자가 존재하지 않으면, UserNotExistsException")
    void givenWrongUser_whenAddToCart_thenThrowUserNotExistsException() {
        Assertions.assertThrows(UserNotExistsException.class, () -> {
            cartService.registerCart("sgoh", null);
        });
    }

    @Test
    @DisplayName("장바구니에 담을 때 매장이 존재하지 않으면, ShopNotExistsException")
    void givenWrongShopId_whenAddToCart_thenThrowShopNotExistsException() {
        UserJpaEntity user = UserJpaEntity.builder().username("username").build();
        memoryUserRepository.save(user);

        CartSaveRequest saveRequest = CartSaveRequest.builder().shopId(1L).build();

        Assertions.assertThrows(ShopNotExistsException.class, () -> {
            cartService.registerCart("username", saveRequest);
        });
    }

    @Test
    @DisplayName("장바구니에 담을 때 메뉴가 존재하지 않으면, MenuNotExistsException")
    void givenWrongMenuId_whenAddToCart_thenThrowMenuNotExistsException() {
        UserJpaEntity user = UserJpaEntity.builder().username("username").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        memoryUserRepository.save(user);
        memoryShopRepository.save(shop);

        CartSaveRequest saveRequest = CartSaveRequest.builder().shopId(1L).menuId(1L).build();

        Assertions.assertThrows(MenuNotExistsException.class, () -> {
            cartService.registerCart("username", saveRequest);
        });
    }

    @Test
    @DisplayName("장바구니에 담을 때 옵션그룹이 존재하지 않으면, OptionGroupNotExistsException")
    void givenWrongOptionGroupId_whenAddToCart_thenThrowOptionGroupNotExistsException() {
        UserJpaEntity user = UserJpaEntity.builder().username("username").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").build();
        memoryUserRepository.save(user);
        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);

        CartSaveRequest saveRequest = CartSaveRequest.builder()
            .shopId(1L)
            .menuId(1L)
            .optionGroups(
                List.of(CartOptionGroupSaveRequest.builder()
                    .optionGroupId(1L)
                    .build())
            ).build();

        Assertions.assertThrows(OptionGroupNotExistsException.class, () -> {
            cartService.registerCart("username", saveRequest);
        });
    }

    @Test
    @DisplayName("장바구니에 담을 때 옵션이 존재하지 않으면, OptionNotExistsException")
    void givenWrongOptionId_whenAddToCart_thenThrowOptionNotExistsException() {
        UserJpaEntity user = UserJpaEntity.builder().username("username").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder().name("부분육 선택").build();
        memoryUserRepository.save(user);
        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);

        CartSaveRequest saveRequest = CartSaveRequest.builder()
            .shopId(1L)
            .menuId(1L)
            .optionGroups(
                List.of(CartOptionGroupSaveRequest.builder()
                    .optionGroupId(1L)
                    .options(List.of(1L))
                    .build())
            ).build();

        Assertions.assertThrows(OptionNotExistsException.class, () -> {
            cartService.registerCart("username", saveRequest);
        });
    }

    @Test
    @DisplayName("기존 장바구니에 다른 매장의 메뉴가 담겨있으면, OtherShopMenuAlreadyExistsException")
    void givenOtherShopMenuAlreadyExists_whenAddToCart_thenThrowOtherShopMenuAlreadyExistsException() {
        UserJpaEntity user = UserJpaEntity.builder().username("username").build();
        ShopJpaEntity bbq = ShopJpaEntity.builder().name("bbq").build();
        ShopJpaEntity goobne = ShopJpaEntity.builder().name("굽네치킨").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder().name("부분육 선택").build();
        OptionJpaEntity option = OptionJpaEntity.builder().name("순살").build();

        CartJpaEntity cart = CartJpaEntity.builder().shop(goobne).build();

        user.addToCart(cart);
        memoryUserRepository.save(user);
        memoryShopRepository.save(bbq);
        memoryShopRepository.save(goobne);
        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup);
        memoryOptionRepository.save(option);

        CartSaveRequest saveRequest = CartSaveRequest.builder()
            .shopId(1L)
            .menuId(1L)
            .optionGroups(
                List.of(CartOptionGroupSaveRequest.builder()
                    .optionGroupId(1L)
                    .options(List.of(1L))
                    .build())
            ).build();

        Assertions.assertThrows(OtherShopMenuAlreadyExistsException.class, () -> {
            cartService.registerCart("username", saveRequest);
        });
    }

    @Test
    @DisplayName("여러 옵션과 옵션그룹들이 선택된 메뉴가 장바구니에 추가될 수 있다")
    void givenSeveralOptionAndOptionGroup_whenAddToCart_thenSuccess() {
        UserJpaEntity user = UserJpaEntity.builder().username("username").build();
        ShopJpaEntity shop = ShopJpaEntity.builder().name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder().name("황올").build();
        OptionGroupJpaEntity optionGroup = OptionGroupJpaEntity.builder().name("부분육 선택").build();
        OptionJpaEntity option = OptionJpaEntity.builder().name("순살").build();
        memoryUserRepository.save(user);
        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);
        // 여러 id값만 필요하므로 동일한 그룹과 옵션을 중복 저장하여 테스트함
        memoryOptionGroupRepository.save(optionGroup);
        memoryOptionGroupRepository.save(optionGroup);
        memoryOptionRepository.save(option);
        memoryOptionRepository.save(option);
        memoryOptionRepository.save(option);
        memoryOptionRepository.save(option);

        CartSaveRequest saveRequest = CartSaveRequest.builder()
            .shopId(1L)
            .menuId(1L)
            .optionGroups(
                List.of(
                    CartOptionGroupSaveRequest.builder()
                        .optionGroupId(1L)
                        .options(List.of(1L, 2L))
                        .build(),
                    CartOptionGroupSaveRequest.builder()
                        .optionGroupId(2L)
                        .options(List.of(3L, 4L))
                        .build())
            ).build();

        Assertions.assertDoesNotThrow(() -> {
            cartService.registerCart("username", saveRequest);
        });
    }

    @Disabled
    @Test
    @DisplayName("기존에 추가된 메뉴와 추가할 메뉴, 옵션그룹, 옵션 중 하나라도 다르면, 새로 장바구니에 추가한다")
    void givenDifferentMenuAndOption_whenAddToCart_thenAddNewCartItem() {

    }

    @Disabled
    @Test
    @DisplayName("옵션그룹과 옵션이 모두 같은 메뉴가 이미 추가되어 있으면, 기존 정보에서 개수만 증가시킨다")
    void test7() {

    }

    // Read Test
    @Disabled
    @Test
    @DisplayName("장바구니에 담은 메뉴, 옵션그룹, 옵션 이름, 개수를 메뉴 단위 목록으로 조회한다")
    void test8() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니 조회 시, 메뉴 목록에 각 금액이 함께 조회된다")
    void test9() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담은 메뉴들의 총 금액 합을 조회한다")
    void test10() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니에 담긴 상태에서 메뉴나 옵션 정보가 수정되면, 수정된 정보로 조회한다")
    void test11() {

    }

    // Update Test
    @Disabled
    @Test
    @DisplayName("메뉴의 수정된 옵션으로 장바구니에 담긴 메뉴 정보를 변경한다")
    void test12() {

    }

    @Disabled
    @Test
    @DisplayName("수정된 정보와 동일한 메뉴가 장바구니에 이미 존재하면, 개수만 증가시킨다")
    void test13() {

    }

    // Delete Test
    @Disabled
    @Test
    @DisplayName("cartId에 해당하는 메뉴를 장바구니에서 삭제한다")
    void test14() {

    }

    @Disabled
    @Test
    @DisplayName("장바구니의 메뉴를 모두 삭제한다")
    void test15() {

    }

    @Disabled
    @Test
    @DisplayName("매장, 메뉴, 옵션그룹, 옵션 중에 하나라도 삭제되면, 해당 정보를 담은 메뉴가 장바구니에서 삭제된다")
    void test16() {

    }
}
