package com.oheat.user.controller;

import com.oheat.user.dto.CartFindResponse;
import com.oheat.user.dto.CartSaveRequest;
import com.oheat.user.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/carts")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> registerCart(@RequestBody CartSaveRequest saveRequest,
        HttpServletRequest http) {

        String username = (String) http.getAttribute("username");
        cartService.registerCart(username, saveRequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<CartFindResponse> findAllByUsername(HttpServletRequest http) {
        String username = (String) http.getAttribute("username");
        return cartService.findAllByUsername(username).stream()
            .map(CartFindResponse::from)
            .toList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCart(@PathVariable(name = "id") Long cartId) {
        cartService.deleteCart(cartId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAllByUsername(HttpServletRequest http) {
        String username = (String) http.getAttribute("username");
        cartService.deleteAllByUsername(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
