package com.albuquerque.map_market_v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// Quando usar: tentativa de cadastrar um produto já existente, com código ou nome único duplicado.
@ResponseStatus(HttpStatus.CONFLICT)
public class ProductAlreadyExistsException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ProductAlreadyExistsException(String e) {
        super(e);
    }
}
