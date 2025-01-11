package com.oheat.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PaymentNotExistsException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public PaymentNotExistsException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
