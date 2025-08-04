package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.ProductErrorMessages;
import com.albuquerque.map_market_v1.logic.ProductValidator;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.ProductPersistence;
import com.albuquerque.map_market_v1.services.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceExceptionTest {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductPersistence persistence;

    @Mock
    private ProductValidator validator;

    @Mock
    private EntityMapper mapper;

    @BeforeEach
    void setUp() {
        service = new ProductService(persistence, validator, mapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenNoProductsFound() {
        //given
        String expectedMessage = ProductErrorMessages.PRODUCTS_NOT_FOUND;

        //when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findAll());
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductDoesNotExist() {
        //given
        Long id = 555L;
        String expectedMessage = ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + id;

        //when
        when(persistence.existResource(id)).thenReturn(false);
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.delete(id));
        String actualMessage = exception.getMessage();

        //then
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
