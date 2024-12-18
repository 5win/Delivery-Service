package com.oheat.food.service;

import com.oheat.food.dto.MenuSaveRequest;
import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionSaveRequest;
import com.oheat.food.dto.OptionUpdateRequest;
import com.oheat.food.entity.MenuJpaEntity;
import com.oheat.food.entity.OptionGroupJpaEntity;
import com.oheat.food.entity.OptionJpaEntity;
import com.oheat.food.entity.ShopJpaEntity;
import com.oheat.food.exception.MenuNotExistsException;
import com.oheat.food.exception.NoOptionGroupException;
import com.oheat.food.exception.OptionGroupNotExistsException;
import com.oheat.food.exception.OptionNotExistsException;
import com.oheat.food.exception.ShopNotExistsException;
import com.oheat.food.repository.MenuRepository;
import com.oheat.food.repository.OptionGroupRepository;
import com.oheat.food.repository.OptionRepository;
import com.oheat.food.repository.ShopRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MenuService {

    private final ShopRepository shopRepository;
    private final MenuRepository menuRepository;
    private final OptionGroupRepository optionGroupRepository;
    private final OptionRepository optionRepository;

    // Menu CRUD
    public void registerMenu(MenuSaveRequest saveRequest) {
        ShopJpaEntity shop = shopRepository.findById(saveRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);

        MenuJpaEntity menu = saveRequest.toEntity(shop);
        shop.addMenu(menu);
        if (menu.isOptionGroupsEmpty()) {
            throw new NoOptionGroupException();
        }
        menuRepository.save(menu);
    }

    public List<MenuJpaEntity> findByShopId(Long shopId) {
        ShopJpaEntity shop = shopRepository.findById(shopId)
            .orElseThrow(ShopNotExistsException::new);

        return shop.getMenuSet().stream().toList();
    }

    @Transactional
    public void updateMenu(MenuUpdateRequest updateRequest) {
        shopRepository.findById(updateRequest.getShopId())
            .orElseThrow(ShopNotExistsException::new);
        MenuJpaEntity menu = menuRepository.findById(updateRequest.getMenuId())
            .orElseThrow(MenuNotExistsException::new);

        menu.updateMenu(updateRequest);
    }

    public void deleteById(Long menuId) {
        menuRepository.findById(menuId)
            .orElseThrow(MenuNotExistsException::new);
        menuRepository.deleteById(menuId);
    }

    // option CRUD
    public void registerOption(OptionSaveRequest optionSaveRequest) {
        OptionGroupJpaEntity optionGroup = optionGroupRepository.findById(
                optionSaveRequest.getOptionGroupId())
            .orElseThrow(OptionGroupNotExistsException::new);

        optionRepository.save(optionSaveRequest.toEntity(optionGroup));
    }

    @Transactional
    public void updateOption(OptionUpdateRequest updateRequest) {
        OptionJpaEntity option = optionRepository.findById(updateRequest.getOptionId())
            .orElseThrow(OptionNotExistsException::new);

        option.updateOptionInfo(updateRequest);
    }

    public void deleteOption(Long optionId) {
        OptionJpaEntity option = optionRepository.findById(optionId)
            .orElseThrow(OptionNotExistsException::new);
        optionRepository.delete(option);
    }
}
