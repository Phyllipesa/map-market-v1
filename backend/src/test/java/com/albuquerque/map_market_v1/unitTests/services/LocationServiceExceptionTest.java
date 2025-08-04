package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.exception.ProductAlreadyExistsException;
import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.LocationErrorMessages;
import com.albuquerque.map_market_v1.exception.messages.ProductErrorMessages;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.LocationPersistence;
import com.albuquerque.map_market_v1.repositories.ProductPersistence;
import com.albuquerque.map_market_v1.services.LocationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.albuquerque.map_market_v1.unitTests.mocks.LocationMockFactory.mockEntity;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LocationServiceExceptionTest {

    @InjectMocks
    private LocationService service;

    @Mock
    private LocationPersistence persistence;

    @Mock
    private ProductPersistence productPersistence;

    @Mock
    private EntityMapper mapper;

    @BeforeEach
    void setup() {
        service = new LocationService(productPersistence, persistence, mapper);
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenProductDoesNotExist() {
        Long locationId = 1L;
        Long productId = 1L;
        String expectedMessage = ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + productId;

        when(productPersistence.findById(productId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> service.subscribingProduct(locationId, productId));

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void shouldThrowProductAlreadyExistsExceptionWhenProductRegisteredInAnotherLocation() {
        Long locationId = 1L;
        Long productId = 1L;
        String expectedMessage = LocationErrorMessages.PRODUCT_REGISTERED_ANOTHER_LOCATION;
        ProductEntity productFromEntity = new ProductEntity(1L, "vassoura", new BigDecimal("14.50"));

        when(productPersistence.findById(productId)).thenReturn(Optional.of(productFromEntity));
        when(persistence.findLocationByProductId(productId)).thenReturn(Optional.of(new LocationEntity()));

        ProductAlreadyExistsException ex = assertThrows(ProductAlreadyExistsException.class,
            () -> service.subscribingProduct(locationId, productId));

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenLocationDoesNotExist() {
        Long locationId = 1L;
        Long productId = 1L;
        String expectedMessage = LocationErrorMessages.LOCATION_NOT_FOUND + locationId;
        ProductEntity productFromEntity = new ProductEntity(1L, "vassoura", new BigDecimal("14.50"));

        when(productPersistence.findById(productId)).thenReturn(Optional.of(productFromEntity));
        when(persistence.findLocationByProductId(productId)).thenReturn(Optional.empty());
        when(persistence.findById(locationId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
            () -> service.subscribingProduct(locationId, productId));

        assertEquals(expectedMessage, ex.getMessage());
    }

    @Test
    void shouldThrowProductAlreadyExistsExceptionWhenLocationAlreadyHasAProduct() {
        Long locationId = 1L;
        Long productId = 1L;
        String expectedMessage = LocationErrorMessages.PRODUCT_REGISTERED_AT_THIS_LOCATION;
        ProductEntity productFromEntity = new ProductEntity(1L, "vassoura", new BigDecimal("14.50"));
        LocationEntity locationFromEntity = mockEntity();

        when(productPersistence.findById(productId)).thenReturn(Optional.of(productFromEntity));
        when(persistence.findLocationByProductId(productId)).thenReturn(Optional.empty());
        when(persistence.findById(locationId)).thenReturn(Optional.of(locationFromEntity));

        ProductAlreadyExistsException ex = assertThrows(ProductAlreadyExistsException.class,
            () -> service.subscribingProduct(locationId, productId));

        assertEquals(expectedMessage, ex.getMessage());
    }
}
