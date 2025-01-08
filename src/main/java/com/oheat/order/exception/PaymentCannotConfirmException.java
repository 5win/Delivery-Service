package com.oheat.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class PaymentCannotConfirmException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public PaymentCannotConfirmException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
