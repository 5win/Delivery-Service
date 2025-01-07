package com.oheat.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PaymentCannotCancelException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public PaymentCannotCancelException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
