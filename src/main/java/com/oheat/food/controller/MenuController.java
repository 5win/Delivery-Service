package com.oheat.food.controller;

import com.oheat.food.dto.MenuFindByShopIdResponse;
import com.oheat.food.dto.MenuSaveRequest;
import com.oheat.food.dto.MenuUpdateRequest;
import com.oheat.food.dto.OptionGroupSaveRequest;
import com.oheat.food.dto.OptionGroupUpdateRequest;
import com.oheat.food.dto.OptionSaveRequest;
import com.oheat.food.dto.OptionUpdateRequest;
import com.oheat.food.service.MenuService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foods/shops/menus")
public class MenuController {

    private final MenuService menuService;

    // Menu CRUD
    @PostMapping
    public ResponseEntity<?> registerMenu(@RequestBody MenuSaveRequest saveRequest) {
        menuService.registerMenu(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public List<MenuFindByShopIdResponse> findByShopId(@PathVariable(name = "id") Long shopId) {
        return menuService.findByShopId(shopId).stream()
            .map(MenuFindByShopIdResponse::from)
            .toList();
    }

    @PutMapping
    public ResponseEntity<?> updateMenu(@RequestBody MenuUpdateRequest updateRequest) {
        menuService.updateMenu(updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable(name = "id") Long menuId) {
        menuService.deleteById(menuId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // OptionGroup CRUD
    @PostMapping("/options/groups")
    public ResponseEntity<?> registerOptionGroup(@RequestBody OptionGroupSaveRequest saveRequest) {
        menuService.registerOptionGroup(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/options/groups")
    public ResponseEntity<?> updateOptionGroup(
        @RequestBody OptionGroupUpdateRequest updateRequest) {
        menuService.updateOptionGroup(updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/options/groups/{id}")
    public ResponseEntity<?> deleteOptionGroup(@PathVariable(name = "id") Long optionGroupId) {
        menuService.deleteOptionGroup(optionGroupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // Option CRUD
    @PostMapping("/options")
    public ResponseEntity<?> registerOption(@RequestBody OptionSaveRequest saveRequest) {
        menuService.registerOption(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/options")
    public ResponseEntity<?> updateOption(@RequestBody OptionUpdateRequest updateRequest) {
        menuService.updateOption(updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/options/{id}")
    public ResponseEntity<?> deleteOption(@PathVariable(name = "id") Long optionId) {
        menuService.deleteOption(optionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
