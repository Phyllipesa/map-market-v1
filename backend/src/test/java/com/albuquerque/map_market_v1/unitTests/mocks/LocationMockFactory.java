package com.albuquerque.map_market_v1.unitTests.mocks;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class LocationMockFactory {

    public static LocationEntity mockEntity() {
        ProductEntity product = new ProductEntity(1L, "Product Name Test", new BigDecimal("14.50"));
        ShelvingUnitEntity shelvingUnit = new ShelvingUnitEntity(1L, 1L, "sideA", "sideB");

        return new LocationEntity(1L, shelvingUnit, "A", 1L, 1L, product);
    }

    public static LocationEntity mockEntityWithNullProduct() {
        ShelvingUnitEntity shelvingUnit = new ShelvingUnitEntity(4L, 4L, "sideA4", "sideB4");

        return new LocationEntity(4L, shelvingUnit, "B", 1L, 1L, null);
    }

    public static List<LocationEntity> mockEntityList() {
        List<LocationEntity> list = new ArrayList<>();
        list.add(mockEntity());
        list.add(new LocationEntity(
            2L,
            new ShelvingUnitEntity(2L, 2L, "sideA2", "sideB2"),
            "B",
            1L,
            1L,
            new ProductEntity(2L, "Product Name Test2", new BigDecimal("20.50"))
        ));
        return list;
    }

    public static LocationDto mockDto() {
        ProductDto product = new ProductDto(1L, "Product Name Test", new BigDecimal("14.50"));
        ShelvingUnitDto shelvingUnit = new ShelvingUnitDto(1L, 1L, "sideA", "sideB");

        return new LocationDto(1L, shelvingUnit, "A", 1L, 1L, product);
    }

    public static LocationDto mockDtoWithNullProduct() {
        ShelvingUnitDto shelvingUnit = new ShelvingUnitDto(4L, 4L, "sideA4", "sideB4");

        return new LocationDto(4L, shelvingUnit, "B", 1L, 1L, null);
    }

    public static List<LocationDto> mockDtoList() {
        List<LocationDto> list = new ArrayList<>();
        list.add(mockDto());
        list.add(new LocationDto(
            2L,
            new ShelvingUnitDto(2L, 2L, "sideA2", "sideB2"),
            "B",
            1L,
            1L,
            new ProductDto(2L, "Product Name Test2", new BigDecimal("20.50"))
        ));
        return list;
    }
}
