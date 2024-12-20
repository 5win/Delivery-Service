package com.oheat.order.service;

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
import com.oheat.order.exception.OtherShopMenuAlreadyExistsException;
import com.oheat.user.entity.UserJpaEntity;
import com.oheat.user.exception.UserNotExistsException;
import com.oheat.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartService {

    private final UserRepository userRepository;
    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;

    public void registerCart(String username, CartSaveRequest saveRequest) {
        UserJpaEntity user = userRepository.findByUsername(username)
            .orElseThrow(UserNotExistsException::new);
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        menuRepository.findById(saveRequest.getMenuId())
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
    }
}
