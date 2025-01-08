package com.oheat.order.controller;

import com.oheat.order.dto.PaymentCancelRequest;
import com.oheat.order.dto.PaymentSaveRequest;
import com.oheat.order.service.TossPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments/toss")
public class TossPaymentController {

    private final TossPaymentService tossPaymentService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentSaveRequest saveRequest) {
        tossPaymentService.confirm(saveRequest.toEntity());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentCancelRequest cancelRequest) {
        tossPaymentService.cancel(cancelRequest);
        return ResponseEntity.ok().build();
    }
}
