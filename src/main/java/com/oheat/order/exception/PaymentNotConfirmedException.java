package com.oheat.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PaymentNotConfirmedException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public PaymentNotConfirmedException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
