package com.oheat.order.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class InvalidPaymentInfoException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public InvalidPaymentInfoException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
