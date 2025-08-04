package com.albuquerque.map_market_v1.exception.handler;

import com.albuquerque.map_market_v1.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public final ResponseEntity<ExceptionResponse> handleInternalServerError(InternalServerErrorException e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ InvalidProductException.class, InvalidShelvingException.class })
    public ResponseEntity<ExceptionResponse> handleInvalidParameterException(Exception e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleResourceNotFound(ResourceNotFoundException e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleProductAlreadyExists(ProductAlreadyExistsException e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidCredentials(InvalidCredentialsException e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidJwtAuthenticationException.class)
    public ResponseEntity<ExceptionResponse> handleInvalidJwtAuthentication(InvalidJwtAuthenticationException e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUsernameNotFound(UsernameNotFoundException e, WebRequest request) {
        return buildResponseEntity(e, request, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ExceptionResponse> buildResponseEntity(Exception e, WebRequest request, HttpStatus status) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(
            new Date(),
            e.getMessage(),
            request.getDescription(false)
        );
        return new ResponseEntity<>(exceptionResponse, status);
    }
}
