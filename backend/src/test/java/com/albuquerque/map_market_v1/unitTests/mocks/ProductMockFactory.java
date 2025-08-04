package com.albuquerque.map_market_v1.unitTests.mocks;

import com.albuquerque.map_market_v1.controllers.dto.input.RequestProductDto;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;

import java.math.BigDecimal;
import java.util.List;

public class ProductMockFactory {

    public static List<ProductDto> mockResponseProductDtoList() {
        return List.of(
            new ProductDto(1L, "Produto A", new BigDecimal("10.00")),
            new ProductDto(2L, "Produto B", new BigDecimal("15.00"))
        );
    }

    public static List<ProductEntity> mockResponseProductEntityList() {
        return List.of(
            new ProductEntity(1L, "Produto A", new BigDecimal("10.00")),
            new ProductEntity(2L, "Produto B", new BigDecimal("15.00"))
        );
    }

    public static RequestProductDto mockRequestProductDto() {
        return new RequestProductDto("Castanha do vamo para", new BigDecimal("20.00"));
    }

    public static ProductDto mockResponseProductDto() {
        return new ProductDto(1L, "Castanha do vamo para", new BigDecimal("20.00"));
    }

    public static ProductEntity mockProductToEntity() {
        return new ProductEntity("Castanha do vamo para", new BigDecimal("20.00"));
    }

    public static ProductEntity mockProductFromEntity() {
        return new ProductEntity(1L, "Castanha do vamo para", new BigDecimal("20.00"));
    }
}
