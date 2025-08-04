package com.albuquerque.map_market_v1.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.Serial;

// Quando usar: A operação de criação falhou, mas não foi causada por dados inválidos, nem por produto duplicado,
// nem por ausência de recurso.
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalServerErrorException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public InternalServerErrorException(String e) {
        super(e);
    }
}
