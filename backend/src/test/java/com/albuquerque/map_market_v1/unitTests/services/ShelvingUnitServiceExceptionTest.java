package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.ShelvingUnitErrorMessages;
import com.albuquerque.map_market_v1.logic.ShelvingUnitValidator;
import com.albuquerque.map_market_v1.mapper.EntityMapper;

import com.albuquerque.map_market_v1.repositories.ShelvingUnitPersistence;
import com.albuquerque.map_market_v1.services.ShelvingUnitService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ShelvingUnitServiceExceptionTest {

    @InjectMocks
    private ShelvingUnitService service;

    @Mock
    private ShelvingUnitPersistence persistence;

    @Mock
    private ShelvingUnitValidator validator;

    @Mock
    private EntityMapper mapper;

    @BeforeEach
    void setUp() {
        service = new ShelvingUnitService(persistence,validator, mapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenNoShelvingUnitsFound() {
        // given
        String expectedMessage = ShelvingUnitErrorMessages.SHELVING_UNITS_NOT_FOUND;

        // when
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> service.findAll());
        String actualMessage = exception.getMessage();

        // then
        assertEquals(expectedMessage, actualMessage);
    }
}
