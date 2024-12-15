package com.oheat.food.controller;

import com.oheat.food.dto.MenuGroupSaveRequest;
import com.oheat.food.dto.MenuGroupUpdateRequest;
import com.oheat.food.dto.MenuSaveToMenuGroupRequest;
import com.oheat.food.service.MenuGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/food/shops/menuGroups")
public class MenuGroupController {

    private final MenuGroupService menuGroupService;

    @PostMapping
    public ResponseEntity<?> registerMenuGroup(@RequestBody MenuGroupSaveRequest saveRequest) {
        menuGroupService.registerMenuGroup(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateMenuGroup(@RequestBody MenuGroupUpdateRequest updateRequest) {
        menuGroupService.updateMenuGroup(updateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteMenuGroup(@PathVariable(name = "id") Long menuGroupId) {
        menuGroupService.deleteMenuGroup(menuGroupId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/menu")
    public ResponseEntity<?> registerMenuToMenuGroup(
        @RequestBody MenuSaveToMenuGroupRequest saveRequest) {
        menuGroupService.registerMenuToMenuGroup(saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/menu/{id}")
    public ResponseEntity<?> deleteMenuFromMenuGroup(@PathVariable(name = "id") Long mappingId) {
        menuGroupService.deleteMenuFromMenuGroup(mappingId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
