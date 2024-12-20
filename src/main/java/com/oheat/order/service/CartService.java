package com.oheat.order.service;

import com.oheat.food.entity.MenuJpaEntity;
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
import com.oheat.order.dto.CartSaveRequest.CartOptionGroupSaveRequest;
import com.oheat.order.entity.CartJpaEntity;
import com.oheat.order.entity.CartOptionGroup;
import com.oheat.order.entity.CartOptionGroupOption;
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

        saveRequest.getOptionGroups().forEach(optionGroup -> {
            optionGroupRepository.findById(optionGroup.getOptionGroupId())
                .orElseThrow(OptionGroupNotExistsException::new);
            optionGroup.getOptions().forEach(optionId -> {
                optionRepository.findById(optionId)
                    .orElseThrow(OptionNotExistsException::new);
            });
        });

        if (user.hasOtherShopMenuInCart(shop)) {
            throw new OtherShopMenuAlreadyExistsException();
        }

        Optional<CartJpaEntity> sameCartItem = findSameCartItem(
            user, menu, saveRequest.getOptionGroups());

        if (sameCartItem.isPresent()) {
            sameCartItem.get().increaseAmount(saveRequest.getAmount());
            return;
        }

    }

    private Optional<CartJpaEntity> findSameCartItem(UserJpaEntity user, MenuJpaEntity menu,
        List<CartOptionGroupSaveRequest> saveOptionGroups) {
        List<CartJpaEntity> carts = cartRepository.findAllByUserAndMenu(user, menu);

        List<CartOptionGroupSaveRequest> sortedSaveOptionGroups = saveOptionGroups.stream()
            .sorted(Comparator.comparing(CartOptionGroupSaveRequest::getOptionGroupId))
            .toList();

        for (CartJpaEntity cart : carts) {
            if (checkOptionGroupSame(cart.getCartOptionGroups(), sortedSaveOptionGroups)) {
                return Optional.of(cart);
            }
        }
        return Optional.empty();
    }

    private boolean checkOptionGroupSame(List<CartOptionGroup> list1,
        List<CartOptionGroupSaveRequest> sortedList2) {

        if (list1.size() != sortedList2.size()) {
            return false;
        }
        List<CartOptionGroup> sortedList1 = list1.stream()
            .sorted(Comparator.comparing(CartOptionGroup::getId))
            .toList();

        for (int i = 0; i < sortedList1.size(); i++) {
            final CartOptionGroup cartOptionGroup = sortedList1.get(i);
            final CartOptionGroupSaveRequest groupRequest = sortedList2.get(i);

            if (!cartOptionGroup.getOptionGroup().getId().equals(groupRequest.getOptionGroupId())) {
                return false;
            }
            if (!checkOptionSame(cartOptionGroup.getCartOptionGroupOptions(),
                groupRequest.getOptions())) {
                return false;
            }
        }
        return true;
    }

    private boolean checkOptionSame(List<CartOptionGroupOption> list1, List<Long> list2) {
        if (list1.size() != list2.size()) {
            return false;
        }

        List<CartOptionGroupOption> sortedList1 = list1.stream()
            .sorted(Comparator.comparing(v -> v.getOption().getId()))
            .toList();
        List<Long> sortedList2 = list2.stream()
            .sorted(Long::compareTo)
            .toList();

        for (int i = 0; i < sortedList1.size(); i++) {
            if (!sortedList1.get(i).getOption().getId().equals(sortedList2.get(i))) {
                return false;
            }
        }
        return true;
    }
}
