package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestProductDto;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.logic.ProductValidator;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.ProductPersistence;
import com.albuquerque.map_market_v1.services.ProductService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.albuquerque.map_market_v1.unitTests.mocks.ProductMockFactory.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

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
    void shouldReturnListOfAllProducts() {
        // given
        List<ProductEntity> entityList = mockResponseProductEntityList();
        List<ProductDto> dtoList = mockResponseProductDtoList();

        when(persistence.findAll()).thenReturn(entityList);
        when(mapper.parseListObject(entityList, ProductDto.class)).thenReturn(dtoList);

        // when
        List<ProductDto> result = service.findAll();

        // then
        assertNotNull(result);

        assertAll("First product",
            () -> assertEquals(1L, result.getFirst().getId()),
            () -> assertEquals("Produto A", result.getFirst().getName()),
            () -> assertEquals(new BigDecimal("10.00"), result.getFirst().getPrice())
        );

        assertAll("Last product",
            () -> assertEquals(2L, result.getLast().getId()),
            () -> assertEquals("Produto B", result.getLast().getName()),
            () -> assertEquals(new BigDecimal("15.00"), result.getLast().getPrice())
        );
    }

    @Test
    void shouldReturnProductWhenIdExists() {
        // given
        Long id = 1L;
        ProductEntity entity = mockProductFromEntity();
        ProductDto expectedDto = mockResponseProductDto();

        when(persistence.findById(id)).thenReturn(Optional.of(entity));
        when(mapper.parseObject(entity, ProductDto.class)).thenReturn(expectedDto);

        // when
        ProductDto result = service.findById(id);

        // then
        assertAll("Product by ID",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals("Castanha do vamo para", result.getName()),
            () -> assertEquals(new BigDecimal("20.00"), result.getPrice())
        );
    }

    @Test
    void shouldCreateProductSuccessfully() {
        // given
        RequestProductDto newProduct = mockRequestProductDto();
        ProductEntity toEntity = mockProductToEntity();
        ProductEntity fromEntity = mockProductFromEntity();
        ProductDto expectDto = mockResponseProductDto();

        doNothing().when(validator).validate(newProduct);
        when(mapper.parseObject(newProduct, ProductEntity.class)).thenReturn(toEntity);
        when(persistence.create(toEntity)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, ProductDto.class)).thenReturn(expectDto);

        // when
        ProductDto result = service.create(newProduct);

        // then
        verify(validator).validate(newProduct);
        verify(mapper).parseObject(newProduct, ProductEntity.class);
        verify(persistence).create(toEntity);
        verify(mapper).parseObject(fromEntity, ProductDto.class);

        assertAll("Create",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals("Castanha do vamo para", result.getName()),
            () -> assertEquals(new BigDecimal("20.00"), result.getPrice())
        );
    }

    @Test
    void shouldUpdateProductSuccessfully() {
        // given
        Long id = 1L;
        RequestProductDto request = mockRequestProductDto();
        ProductEntity toUpdate = mockProductToEntity();
        ProductEntity fromEntity = mockProductFromEntity();
        ProductDto expectDto = mockResponseProductDto();

        doNothing().when(validator).validate(request);
        when(persistence.findById(id)).thenReturn(Optional.of(toUpdate));
        when(persistence.update(toUpdate)).thenReturn(Optional.of(fromEntity));
        when(mapper.parseObject(fromEntity, ProductDto.class)).thenReturn(expectDto);

        // when
        ProductDto result = service.update(id, request);

        // then
        verify(validator).validate(request);
        verify(persistence).update(toUpdate);
        verify(mapper).parseObject(fromEntity, ProductDto.class);

        assertAll("Update",
            () -> assertNotNull(result),
            () -> assertEquals(1L, result.getId()),
            () -> assertEquals("Castanha do vamo para", result.getName()),
            () -> assertEquals(new BigDecimal("20.00"), result.getPrice())
        );
    }

    @Test
    void shouldDeleteProductSuccessfully() {
        // given
        Long id = 1L;

        when(persistence.existResource(1L)).thenReturn(true);

        // when
        service.delete(id);

        // then
        verify(persistence, times(1)).existResource(id);
        verify(persistence, times(1)).delete(id);
    }
}
