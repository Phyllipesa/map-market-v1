package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
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
import java.util.List;
import java.util.Optional;

import static com.albuquerque.map_market_v1.unitTests.mocks.LocationMockFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

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
    void shouldReturnListOfAllLocations() {
        // given
        List<LocationEntity> locationEntities = mockEntityList();
        List<LocationDto> locationDtos = mockDtoList();

        // when
        when(persistence.findAll()).thenReturn(locationEntities);
        when(mapper.parseListObject(locationEntities, LocationDto.class)).thenReturn(locationDtos);

        List<LocationDto> res = service.findAll();

        // then
        verify(persistence).findAll();
        verify(mapper).parseListObject(locationEntities, LocationDto.class);

        assertEquals(2, res.size());

        LocationDto firstLocation = res.getFirst();
        assertAll("First Location",
            () -> assertEquals(1L, firstLocation.getId()),
            () -> assertEquals(1L, firstLocation.getShelvingUnitId()),
            () -> assertEquals("A", firstLocation.getSide()),
            () -> assertEquals(1L, firstLocation.getPart()),
            () -> assertEquals(1L, firstLocation.getShelf()),
            () -> assertEquals(1L, firstLocation.getProduct().getId()),
            () -> assertEquals("Product Name Test", firstLocation.getProduct().getName()),
            () -> assertEquals(new BigDecimal("14.50"), firstLocation.getProduct().getPrice())
        );

        LocationDto lastLocation = res.getLast();
        assertAll("Last Location",
            () -> assertEquals(2L, lastLocation.getId()),
            () -> assertEquals(2L, lastLocation.getShelvingUnitId()),
            () -> assertEquals("B", lastLocation.getSide()),
            () -> assertEquals(1L, lastLocation.getPart()),
            () -> assertEquals(1L, lastLocation.getShelf()),
            () -> assertEquals(2L, lastLocation.getProduct().getId()),
            () -> assertEquals("Product Name Test2", lastLocation.getProduct().getName()),
            () -> assertEquals(new BigDecimal("20.50"), lastLocation.getProduct().getPrice())
        );
    }

    @Test
    void shouldReturnLocationWhenIdExists() {
        // given
        Long id = 1L;
        LocationEntity fromEntity = mockEntity();
        LocationDto dto = mockDto();

        // when
        when(persistence.findById(id)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, LocationDto.class)).thenReturn(dto);
        LocationDto res = service.findById(id);

        // then
        assertAll("FindById",
            () -> assertNotNull(res),
            () -> assertEquals(1L, res.getId()),
            () -> assertEquals(1L, res.getShelvingUnitId()),
            () -> assertEquals("A", res.getSide()),
            () -> assertEquals(1L, res.getPart()),
            () -> assertEquals(1L, res.getShelf()),
            () -> assertEquals(1L, res.getProduct().getId()),
            () -> assertEquals("Product Name Test", res.getProduct().getName()),
            () -> assertEquals(new BigDecimal("14.50"), res.getProduct().getPrice())
        );
    }

    @Test
    void shouldRegisterProductInLocationSuccessfully() {
        // given
        Long locationId = 1L;
        Long productId = 1L;
        LocationEntity locationFromEntity = mockEntityWithNullProduct();
        ProductEntity productFromEntity = new ProductEntity(1L, "Product Name Test", new BigDecimal("14.50"));
        LocationDto locationDto = mockDto();

        // when
        when(productPersistence.findById(productId)).thenReturn(Optional.of(productFromEntity));
        when(persistence.findLocationByProductId(productId)).thenReturn(Optional.empty());
        when(persistence.findById(locationId)).thenReturn(Optional.of(locationFromEntity));
        when(persistence.update(locationFromEntity)).thenReturn(Optional.of(locationFromEntity));
        when(mapper.parseObject(locationFromEntity, LocationDto.class)).thenReturn(locationDto);

        LocationDto res = service.subscribingProduct(locationId, productId);

        // then
        assertAll("Subscribing product",
            () -> assertNotNull(res),
            () -> assertEquals(1L, res.getId()),
            () -> assertEquals(1L, res.getShelvingUnitId()),
            () -> assertEquals("A", res.getSide()),
            () -> assertEquals(1L, res.getPart()),
            () -> assertEquals(1L, res.getShelf()),
            () -> assertEquals(1L, res.getProduct().getId()),
            () -> assertEquals("Product Name Test", res.getProduct().getName()),
            () -> assertEquals(new BigDecimal("14.50"), res.getProduct().getPrice())
        );
    }

    @Test
    void shouldUnregisterProductFromLocationSuccessfully() {
        // given
        Long locationId = 1L;
        LocationEntity location = mockEntity();
        LocationDto dto = mockDtoWithNullProduct();

        // when
        when(persistence.findById(locationId)).thenReturn(Optional.of(location));
        when(persistence.update(location)).thenReturn(Optional.of(location));
        when(mapper.parseObject(location, LocationDto.class)).thenReturn(dto);

        LocationDto res = service.unsubscribingProduct(locationId);

        // then
        verify(persistence).findById(locationId);
        verify(persistence).update(location);
        verify(mapper).parseObject(location, LocationDto.class);

        assertAll("unsubscribing a product",
            () -> assertNotNull(res),
            () -> assertEquals(4L, res.getId()),
            () -> assertEquals(4L, res.getShelvingUnitId()),
            () -> assertEquals("B", res.getSide()),
            () -> assertEquals(1L, res.getPart()),
            () -> assertEquals(1L, res.getShelf()),
            () -> assertNull(res.getProduct())
        );
    }
}
