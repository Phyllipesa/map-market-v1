package com.albuquerque.map_market_v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// Quando usar: dados inválidos, como nome vazio, preço negativo, estoque negativo, etc.
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidProductException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InvalidProductException(String e) {
        super(e);
    }
}