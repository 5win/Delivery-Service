package com.oheat.order.controller;

import com.oheat.order.dto.OrderFindByIdResponse;
import com.oheat.order.dto.OrderFindByUserResponse;
import com.oheat.order.dto.OrderSaveRequest;
import com.oheat.order.entity.Order;
import com.oheat.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> registerOrder(@RequestBody OrderSaveRequest saveRequest,
        HttpServletRequest http) {
        String username = (String) http.getAttribute("username");
        orderService.registerOrder(saveRequest, username);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public Page<OrderFindByUserResponse> findOrderByUser(Pageable pageable,
        HttpServletRequest http) {

        String username = (String) http.getAttribute("username");
        return orderService.findOrderByUser(username, pageable)
            .map(OrderFindByUserResponse::from);
    }

    @GetMapping("/{id}")
    public OrderFindByIdResponse findOrderById(@PathVariable(name = "id") UUID orderId) {
        Order order = orderService.findOrderById(orderId);
        return OrderFindByIdResponse.from(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderHistoryById(@PathVariable(name = "id") UUID orderId) {
        orderService.deleteOrderHistoryById(orderId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
