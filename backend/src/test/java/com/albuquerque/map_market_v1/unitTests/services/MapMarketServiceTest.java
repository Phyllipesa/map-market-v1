package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.MapMarketPersistence;
import com.albuquerque.map_market_v1.services.MapMarketService;

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
import static com.albuquerque.map_market_v1.unitTests.mocks.ProductMockFactory.*;
import static com.albuquerque.map_market_v1.unitTests.mocks.ShelvingUnitMockFactory.mockResponseShelvingUnitDtoList;
import static com.albuquerque.map_market_v1.unitTests.mocks.ShelvingUnitMockFactory.mockResponseShelvingUnitEntityList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MapMarketServiceTest {

    @InjectMocks
    private MapMarketService service;

    @Mock
    private MapMarketPersistence persistence;

    @Mock
    private EntityMapper mapper;

    @BeforeEach
    void setup() {
        service = new MapMarketService(persistence, mapper);
    }

    @Test
    void shouldReturnListOfAllRegisteredProducts() {
        // given
        List<ProductEntity> entityList = mockResponseProductEntityList();
        List<ProductDto> productDtoList = mockResponseProductDtoList();

        // when
        when(persistence.findRegisteredProducts()).thenReturn(entityList);
        when(mapper.parseListObject(entityList, ProductDto.class)).thenReturn(productDtoList);

        List<ProductDto> response = service.findRegisteredProducts();

        // then
        assertNotNull(response);

        assertAll("First product",
            () -> assertEquals(1L, response.getFirst().getId()),
            () -> assertEquals("Produto A", response.getFirst().getName()),
            () -> assertEquals(new BigDecimal("10.00"), response.getFirst().getPrice())
        );

        assertAll("Last product",
            () -> assertEquals(2L, response.getLast().getId()),
            () -> assertEquals("Produto B", response.getLast().getName()),
            () -> assertEquals(new BigDecimal("15.00"), response.getLast().getPrice())
        );
    }

    @Test
    void shouldReturnListOfAllLocationsWithProductIsNotNull() {
        // given
        List<LocationEntity> entityList = mockEntityList();
        List<LocationDto> locationDtoList = mockDtoList();

        // when
        when(persistence.findByProductIsNotNull()).thenReturn(entityList);
        when(mapper.parseListObject(entityList, LocationDto.class)).thenReturn(locationDtoList);

        List<LocationDto> response = service.findByProductIsNotNull();

        // then
        assertEquals(2, response.size());

        LocationDto firstLocation = response.getFirst();
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

        LocationDto lastLocation = response.getLast();
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
    void shouldReturnLocationByProductIdWhenIdExists() {
        // given
        Long id = 1L;
        LocationEntity fromEntity = mockEntity();
        LocationDto dto = mockDto();

        // when
        when(persistence.findLocationByProductId(id)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, LocationDto.class)).thenReturn(dto);
        LocationDto res = service.findLocationByProductId(id);

        // then
        assertAll("Find location by product_id",
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
    void shouldReturnListOfAllShelvingUnitsWithRegisteredProducts() {
        // given
        List<ShelvingUnitEntity> entityList = mockResponseShelvingUnitEntityList();
        List<ShelvingUnitDto> dtoList = mockResponseShelvingUnitDtoList();

        when(persistence.findUnitsWithAssignedProducts()).thenReturn(entityList);
        when(mapper.parseListObject(entityList, ShelvingUnitDto.class)).thenReturn(dtoList);

        // when
        List<ShelvingUnitDto> res = service.findUnitsWithAssignedProducts();

        // then
        assertEquals(2, res.size());

        ShelvingUnitDto first = res.get(0);
        assertAll("First shelving unit",
            () -> assertEquals(1L, first.getId()),
            () -> assertEquals(1L, first.getUnit()),
            () -> assertEquals("Grãos", first.getSideA()),
            () -> assertEquals("Farinhas", first.getSideB())
        );

        ShelvingUnitDto last = res.get(1);
        assertAll("Second shelving unit",
            () -> assertEquals(2L, last.getId()),
            () -> assertEquals(2L, last.getUnit()),
            () -> assertEquals("Bolachas e Biscoitos", last.getSideA()),
            () -> assertEquals("Sobremesas e Doces", last.getSideB())
        );
    }
}
