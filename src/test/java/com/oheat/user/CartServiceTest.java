package com.oheat.user;

import static org.assertj.core.api.Assertions.assertThat;

import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionUpdateRequest;
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
import com.oheat.user.dto.CartSaveRequest;
import com.oheat.user.dto.CartSaveRequest.CartOptionGroupSaveRequest;
import com.oheat.user.entity.CartJpaEntity;
import com.oheat.user.entity.CartOptionGroup;
import com.oheat.user.entity.CartOptionGroupOption;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.CartNotExistsException;
import com.oheat.user.exception.OtherShopMenuAlreadyExistsException;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.CartRepository;
import com.oheat.user.repository.UserRepository;
import com.oheat.user.service.CartService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartServiceTest {

    private final UserRepository memoryUserRepository = new MemoryUserRepository();
    private final ShopRepository memoryShopRepository = new MemoryShopRepository();
    private final MenuRepository memoryMenuRepository = new MemoryMenuRepository();
    private final OptionGroupRepository memoryOptionGroupRepository = new MemoryOptionGroupRepository();
    private final OptionRepository memoryOptionRepository = new MemoryOptionRepository();
    private final CartRepository memoryCartRepository = new MemoryCartRepository();
    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartService = new CartService(memoryUserRepository, memoryShopRepository,
            memoryMenuRepository, memoryOptionGroupRepository, memoryOptionRepository,
            memoryCartRepository);
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

    // Read Test
    @Test
    @DisplayName("장바구니에 담은 메뉴, 옵션그룹, 옵션 이름, 개수를 메뉴 단위 목록으로 조회한다")
    void givenCartItem_whenFindByUserId_thenReturn() {
        registerOneCartItem();

        CartJpaEntity cartResult = cartService.findAllByUsername("username").getFirst();

        CartOptionGroup groupResult1 = cartResult.getCartOptionGroups().get(0);
        CartOptionGroup groupResult2 = cartResult.getCartOptionGroups().get(1);
        OptionJpaEntity optionResult1 = groupResult1.getCartOptionGroupOptions().get(0).getOption();
        OptionJpaEntity optionResult3 = groupResult2.getCartOptionGroupOptions().get(0).getOption();
        OptionJpaEntity optionResult4 = groupResult2.getCartOptionGroupOptions().get(1).getOption();

        assertThat(cartResult.getShop().getName()).isEqualTo("bbq");
        assertThat(cartResult.getMenu().getName()).isEqualTo("황올");
        assertThat(groupResult1.getOptionGroup().getName()).isEqualTo("부분육 선택");
        assertThat(groupResult2.getOptionGroup().getName()).isEqualTo("음료 선택");
        assertThat(optionResult1.getName()).isEqualTo("순살");
        assertThat(optionResult3.getName()).isEqualTo("콜라");
        assertThat(optionResult4.getName()).isEqualTo("사이다");
    }

    @Test
    @DisplayName("장바구니 조회 시, 메뉴 목록에 각 금액이 함께 조회된다")
    void givenCartItem_whenFindByUsername_thenReturnPriceOfEachMenu() {
        registerOneCartItem();

        int priceOfMenu = cartService.findAllByUsername("username").getFirst()
            .calcPriceOfMenu();

        assertThat(priceOfMenu).isEqualTo(28_000);
    }

    @Test
    @DisplayName("장바구니의 메뉴 항목 개수가 3개이면, 메뉴 가격 * 3의 가격이 반환된다")
    void givenCartItemWithAmount3_whenFindByUsername_thenReturn3TimesOfPrice() {
        registerOneCartItem();
        CartJpaEntity cart = memoryCartRepository.findById(1L).get();
        cart.increaseAmount(2);     // 기존 개수는 1개

        int priceOfMenu = cartService.findAllByUsername("username").getFirst()
            .calcPriceOfMenu();

        assertThat(priceOfMenu).isEqualTo(28_000 * 3);
    }

    @Test
    @DisplayName("장바구니에 담긴 상태에서 메뉴나 옵션 정보가 수정되면, 수정된 정보로 조회한다")
    void givenCartItem_whenChangeMenuAndOptionInfo_thenReturnUpdatedInfo() {
        registerOneCartItem();

        MenuJpaEntity menu = memoryMenuRepository.findById(1L).get();
        OptionJpaEntity option = memoryOptionRepository.findById(1L).get();

        MenuUpdateRequest menuUpdateReq = MenuUpdateRequest.builder()
            .name("땡초 양념 치킨").price(50_000).build();
        OptionUpdateRequest optionUpdateReq = OptionUpdateRequest.builder()
            .name("윙,봉 콤보").price(7_000).build();

        menu.updateMenu(menuUpdateReq);
        option.updateOptionInfo(optionUpdateReq);

        CartJpaEntity cartResult = cartService.findAllByUsername("username").getFirst();
        OptionJpaEntity option1Result = cartResult.getCartOptionGroups().getFirst()
            .getCartOptionGroupOptions().getFirst()
            .getOption();

        // 기존 가격은 28,000원
        assertThat(cartResult.calcPriceOfMenu()).isEqualTo(61_000);
        assertThat(cartResult.getMenu().getName()).isEqualTo("땡초 양념 치킨");
        assertThat(option1Result.getName()).isEqualTo("윙,봉 콤보");
    }

    // Delete Test
    @Test
    @DisplayName("cartId에 해당하는 장바구니 항목이 없으면, CartNotExistsException")
    void givenWrongCartId_whenDeleteCart_thenThrowCartNotExistsException() {
        Assertions.assertThrows(CartNotExistsException.class, () -> {
            cartService.deleteCart(1L);
        });
    }

    @Test
    @DisplayName("cartId에 해당하는 메뉴를 장바구니에서 삭제한다")
    void givenCartId_whenDeleteCart_thenSuccess() {
        registerOneCartItem();

        Assertions.assertDoesNotThrow(() -> {
            cartService.deleteCart(1L);
        });

        Optional<CartJpaEntity> result = memoryCartRepository.findById(1L);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("장바구니의 메뉴를 모두 삭제한다")
    void whenDeleteAllCartByUsername_thenFindAllByUsernameReturnZero() {
        registerOneCartItem();

        cartService.deleteAllByUsername("username");
        List<CartJpaEntity> username = cartService.findAllByUsername("username");
        assertThat(username.size()).isZero();
    }

    void registerOneCartItem() {
        UserJpaEntity user = UserJpaEntity.builder()
            .username("username").build();
        ShopJpaEntity shop = ShopJpaEntity.builder()
            .name("bbq").build();
        MenuJpaEntity menu = MenuJpaEntity.builder()
            .name("황올").price(20_000).shop(shop).build();
        OptionGroupJpaEntity optionGroup1 = OptionGroupJpaEntity.builder()
            .name("부분육 선택").menu(menu).build();
        OptionGroupJpaEntity optionGroup2 = OptionGroupJpaEntity.builder()
            .name("음료 선택").menu(menu).build();
        OptionJpaEntity option1 = OptionJpaEntity.builder()
            .name("순살").price(4_000).optionGroup(optionGroup1).build();
        OptionJpaEntity option2 = OptionJpaEntity.builder()
            .name("콜라").price(2_000).optionGroup(optionGroup2).build();
        OptionJpaEntity option3 = OptionJpaEntity.builder()
            .name("사이다").price(2_000).optionGroup(optionGroup2).build();

        CartJpaEntity cart = CartJpaEntity.builder()
            .amount(1).user(user).shop(shop).menu(menu).build();
        CartOptionGroup cartOptionGroup1 = CartOptionGroup.builder()
            .cart(cart).optionGroup(optionGroup1).build();
        CartOptionGroup cartOptionGroup2 = CartOptionGroup.builder()
            .cart(cart).optionGroup(optionGroup2).build();
        CartOptionGroupOption cartOptionGroupOption1 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup1).option(option1).build();
        CartOptionGroupOption cartOptionGroupOption2 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup2).option(option2).build();
        CartOptionGroupOption cartOptionGroupOption3 = CartOptionGroupOption.builder()
            .cartOptionGroup(cartOptionGroup2).option(option3).build();

        cartOptionGroup1.addCartOption(cartOptionGroupOption1);
        cartOptionGroup2.addCartOption(cartOptionGroupOption2);
        cartOptionGroup2.addCartOption(cartOptionGroupOption3);
        cart.addCartOptionGroup(cartOptionGroup1);
        cart.addCartOptionGroup(cartOptionGroup2);
        user.addToCart(cart);

        memoryUserRepository.save(user);
        memoryShopRepository.save(shop);
        memoryMenuRepository.save(menu);
        memoryOptionGroupRepository.save(optionGroup1);
        memoryOptionGroupRepository.save(optionGroup2);
        memoryOptionRepository.save(option1);
        memoryOptionRepository.save(option2);
        memoryOptionRepository.save(option2);
        memoryOptionRepository.save(option3);
        memoryCartRepository.save(cart);
    }
}
