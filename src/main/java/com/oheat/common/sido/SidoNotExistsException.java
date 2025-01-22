package com.oheat.common.sido;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class SidoNotExistsException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public SidoNotExistsException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
