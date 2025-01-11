package com.oheat.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class AddressNotExistsException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public AddressNotExistsException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
