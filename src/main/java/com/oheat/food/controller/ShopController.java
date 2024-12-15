package com.oheat.food.controller;

import com.oheat.food.dto.ShopFindByCategoryResponse;
import com.oheat.food.dto.ShopSaveRequest;
import com.oheat.food.dto.ShopUpdateRequest;
import com.oheat.food.service.ShopService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/food/shops")
public class ShopController {

    private final ShopService shopService;

    @PostMapping
    public ResponseEntity<?> registerShop(@RequestBody ShopSaveRequest saveRequest) {
        shopService.registerShop(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<ShopFindByCategoryResponse> findShopByCategory(@RequestParam String category) {
        return shopService.findShopByCategory(category).stream()
            .map(ShopFindByCategoryResponse::from)
            .toList();
    }

    @PutMapping
    public ResponseEntity<?> updateShop(@RequestBody ShopUpdateRequest updateRequest) {
        shopService.updateShop(updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteShop(@PathVariable(name = "id") Long shopId) {
        shopService.deleteShop(shopId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
