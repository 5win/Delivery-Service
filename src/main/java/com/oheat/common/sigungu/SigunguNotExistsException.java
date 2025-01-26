package com.oheat.common.sigungu;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class SigunguNotExistsException extends RuntimeException {

    private final HttpStatusCode statusCode;

    public SigunguNotExistsException(HttpStatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
