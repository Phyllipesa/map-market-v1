package com.albuquerque.map_market_v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParameterNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ParameterNotFoundException(String message) {
        super(message);
    }
    public ParameterNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
