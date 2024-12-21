package com.oheat.order.service;

import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.OptionGroupNotExistsException;
import com.oheat.food.exception.OptionNotExistsException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.OptionGroupRepository;
import com.oheat.food.repository.OptionRepository;
import com.oheat.food.repository.ShopRepository;
import com.oheat.order.dto.CartSaveRequest;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.order.entity.CartOptionGroup;
import com.oheat.order.entity.CartOptionGroupOption;
import com.oheat.order.exception.CannotCreateCartException;
import com.oheat.order.exception.OtherShopMenuAlreadyExistsException;
import com.oheat.order.repository.CartRepository;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;
    private final CartRepository cartRepository;

    @Transactional
    public void registerCart(String username, CartSaveRequest saveRequest) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        MenuJpaEntity menu = menuRepository.findById(saveRequest.getMenuId())
            .orElseThrow(MenuNotExistsException::new);

        if (user.hasOtherShopMenuInCart(shop)) {
            throw new OtherShopMenuAlreadyExistsException();
        }

        CartJpaEntity cart = generateCart(saveRequest, user, shop, menu)
            .orElseThrow(CannotCreateCartException::new);

        Optional<CartJpaEntity> sameCartItem = findSameCartItem(cart);

        if (sameCartItem.isPresent()) {
            sameCartItem.get()
                .increaseAmount(saveRequest.getAmount());
            return;
        }

    }

    private Optional<CartJpaEntity> generateCart(CartSaveRequest saveRequest, UserJpaEntity user,
        ShopJpaEntity shop, MenuJpaEntity menu) {
        CartJpaEntity cart = saveRequest.toEntity(user, shop, menu);

        saveRequest.getOptionGroups().forEach(groupInfo -> {
            OptionGroupJpaEntity optionGroup = optionGroupRepository
                .findById(groupInfo.getOptionGroupId())
                .orElseThrow(OptionGroupNotExistsException::new);

            CartOptionGroup cartOptionGroup = CartOptionGroup.builder()
                .cart(cart)
                .optionGroup(optionGroup)
                .build();

            groupInfo.getOptions().forEach(optionId -> {
                OptionJpaEntity option = optionRepository.findById(optionId)
                    .orElseThrow(OptionNotExistsException::new);

                CartOptionGroupOption cartOption = CartOptionGroupOption.builder()
                    .cartOptionGroup(cartOptionGroup)
                    .option(option)
                    .build();

                cartOptionGroup.addCartOption(cartOption);
            });
            cart.addCartOptionGroup(cartOptionGroup);
        });
        return Optional.of(cart);
    }

    /**
     * 장바구니에서 동일한 항목을 찾아 반환하는 메서드
     *
     * @param newCart
     * @return 존재하면 해당 항목을 담아서 반환
     */
    private Optional<CartJpaEntity> findSameCartItem(CartJpaEntity newCart) {
        List<CartJpaEntity> carts = cartRepository
            .findAllByUserAndMenu(newCart.getUser(), newCart.getMenu());

        // 유저의 장바구니에 동일한 항목이 존재하는지 확인
        for (CartJpaEntity cart : carts) {
            if (checkOptionGroupSame(cart.getCartOptionGroups(), newCart.getCartOptionGroups())) {
                return Optional.of(cart);
            }
        }
        return Optional.empty();
    }

    /**
     * 두 장바구니 항목의 옵션 그룹이 동일한지 검증하는 메서드
     *
     * @param list1
     * @param list2
     * @return 동일하면 true, 동일하지 않으면 false
     */
    private boolean checkOptionGroupSame(List<CartOptionGroup> list1, List<CartOptionGroup> list2) {
        // 옵션 그룹의 개수가 다르면, false
        if (list1.size() != list2.size()) {
            return false;
        }

        // 옵션 그룹 id로 정렬
        List<CartOptionGroup> sortedList1 = list1.stream()
            .sorted(Comparator.comparing(v -> v.getOptionGroup().getId()))
            .toList();
        List<CartOptionGroup> sortedList2 = list2.stream()
            .sorted(Comparator.comparing(v -> v.getOptionGroup().getId()))
            .toList();

        for (int i = 0; i < sortedList1.size(); i++) {
            final CartOptionGroup cartGroup1 = sortedList1.get(i);
            final CartOptionGroup cartGroup2 = sortedList2.get(i);

            // 옵션 그룹이 하나라도 같지 않으면, false
            if (!cartGroup1.getOptionGroup().equals(cartGroup2.getOptionGroup())) {
                return false;
            }
            // 옵션 그룹에 속한 옵션들이 동일하지 않으면, false
            if (!checkOptionSame(
                cartGroup1.getCartOptionGroupOptions(),
                cartGroup2.getCartOptionGroupOptions())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 두 옵션 그룹의 선택된 옵션들이 동일한지 검증하는 메서드
     *
     * @param list1
     * @param list2
     * @return 동일하면 true, 그렇지 않으면 false
     */
    private boolean checkOptionSame(List<CartOptionGroupOption> list1,
        List<CartOptionGroupOption> list2) {
        // 옵션 개수가 다르면, false
        if (list1.size() != list2.size()) {
            return false;
        }

        // 옵션들을 id로 정렬
        List<CartOptionGroupOption> sortedList1 = list1.stream()
            .sorted(Comparator.comparing(v -> v.getOption().getId()))
            .toList();
        List<CartOptionGroupOption> sortedList2 = list2.stream()
            .sorted(Comparator.comparing(v -> v.getOption().getId()))
            .toList();

        for (int i = 0; i < sortedList1.size(); i++) {
            final OptionJpaEntity option1 = sortedList1.get(i).getOption();
            final OptionJpaEntity option2 = sortedList2.get(i).getOption();
            // 옵션이 하나라도 같지 않으면, false
            if (!option1.equals(option2)) {
                return false;
            }
        }
        return true;
    }
}
