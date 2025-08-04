package com.albuquerque.map_market_v1.services;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ProductDto;
import com.albuquerque.map_market_v1.entities.dtos.output.ShelvingUnitDto;
import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.LocationErrorMessages;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.MapMarketPersistence;
import com.albuquerque.map_market_v1.repositories.interfaces.input.map.FindRegisteredProductsServiceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.map.FindUnitsWithAssignedProductsServiceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.map.MapMarketServiceUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MapMarketService implements
    MapMarketServiceUseCase<LocationDto>,
    FindUnitsWithAssignedProductsServiceUseCase<ShelvingUnitDto>,
    FindRegisteredProductsServiceUseCase<ProductDto> {

    private final MapMarketPersistence persistence;
    private final EntityMapper mapper;

    public MapMarketService(MapMarketPersistence persistence, EntityMapper mapper) {
        this.persistence = persistence;
        this.mapper = mapper;
    }

    @Override
    public List<ProductDto> findRegisteredProducts() {
        return mapper.parseListObject(
            persistence.findRegisteredProducts(), ProductDto.class
        );
    }

    @Override
    public List<LocationDto> findByProductIsNotNull() {
        return  mapper.parseListObject(
            persistence.findByProductIsNotNull(), LocationDto.class
        );
    }

    @Override
    public LocationDto findLocationByProductId(Long id) {
        LocationEntity entity = persistence.findLocationByProductId(id)
            .orElseThrow(() -> new ResourceNotFoundException(LocationErrorMessages.ERROR_PRODUCT_IN_LOCATION_NOT_FOUND + id));
        return mapper.parseObject(entity, LocationDto.class);
    }

    @Override
    public List<ShelvingUnitDto> findUnitsWithAssignedProducts() {
        return  mapper.parseListObject(
            persistence.findUnitsWithAssignedProducts(), ShelvingUnitDto.class
        );
    }
}
