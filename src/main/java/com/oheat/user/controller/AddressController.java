package com.oheat.user.controller;

import com.oheat.user.dto.AddressFindResponse;
import com.oheat.user.dto.AddressSaveRequest;
import com.oheat.user.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/addresses")
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<?> registerAddress(@RequestBody AddressSaveRequest saveRequest, HttpServletRequest http) {
        String username = (String) http.getAttribute("username");
        addressService.registerAddress(saveRequest, username);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public List<AddressFindResponse> findAllAddressByUsername(HttpServletRequest http) {
        String username = (String) http.getAttribute("username");
        return addressService.findAllByUsername(username);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> changeSelectedAddress(@PathVariable("id") Long addressId, HttpServletRequest http) {
        String username = (String) http.getAttribute("username");
        addressService.changeSelectedAddress(addressId, username);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
