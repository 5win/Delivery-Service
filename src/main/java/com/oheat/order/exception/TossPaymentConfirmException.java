package com.oheat.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class TossPaymentConfirmException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public TossPaymentConfirmException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
