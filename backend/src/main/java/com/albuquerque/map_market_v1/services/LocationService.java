package com.albuquerque.map_market_v1.services;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.LocationDto;
import com.albuquerque.map_market_v1.exception.InternalServerErrorException;
import com.albuquerque.map_market_v1.exception.ProductAlreadyExistsException;
import com.albuquerque.map_market_v1.exception.ResourceNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.LocationErrorMessages;
import com.albuquerque.map_market_v1.exception.messages.ProductErrorMessages;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.LocationPersistence;
import com.albuquerque.map_market_v1.repositories.ProductPersistence;
import com.albuquerque.map_market_v1.repositories.interfaces.input.location.LocationServiceUseCase;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService implements LocationServiceUseCase<LocationDto> {

    private final ProductPersistence productPersistence;
    private final LocationPersistence persistence;
    private final EntityMapper mapper;

    public LocationService(
        ProductPersistence productPersistence,
        LocationPersistence persistence,
        EntityMapper mapper
    ) {
        this.productPersistence = productPersistence;
        this.persistence = persistence;
        this.mapper = mapper;
    }

    @Override
    public List<LocationDto> findAll() {
        return mapper.parseListObject(
            persistence.findAll(), LocationDto.class
        );
    }

    @Override
    public LocationDto findById(Long id) {
        LocationEntity entity = persistence.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException(LocationErrorMessages.LOCATION_NOT_FOUND + id));
        return mapper.parseObject(entity, LocationDto.class);
    }

    @Override
    public LocationDto subscribingProduct(Long locationId, Long productId) {
        ProductEntity product = productPersistence.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException(ProductErrorMessages.PRODUCT_NOT_FOUND_WITH_ID + productId));

        if (persistence.findLocationByProductId(productId).isPresent()) {
            throw new ProductAlreadyExistsException(LocationErrorMessages.PRODUCT_REGISTERED_ANOTHER_LOCATION);
        }

        LocationEntity location = persistence.findById(locationId)
            .orElseThrow(() -> new ResourceNotFoundException(LocationErrorMessages.LOCATION_NOT_FOUND + locationId));

        if (location.getProduct() != null) {
            throw new ProductAlreadyExistsException(LocationErrorMessages.PRODUCT_REGISTERED_AT_THIS_LOCATION);
        }
        location.setProduct(product);

        LocationEntity locationUpdated = persistence.update(location)
            .orElseThrow(() -> new InternalServerErrorException(LocationErrorMessages.INTERNAL_SERVER_ERROR));
        return mapper.parseObject(locationUpdated, LocationDto.class);
    }

    @Override
    public LocationDto unsubscribingProduct(Long locationId) {
        LocationEntity entity = persistence.findById(locationId)
            .orElseThrow(() -> new ResourceNotFoundException(LocationErrorMessages.LOCATION_NOT_FOUND + locationId));
        entity.setProduct(null);
        LocationEntity response = persistence.update(entity)
            .orElseThrow(() -> new InternalServerErrorException(LocationErrorMessages.INTERNAL_SERVER_ERROR));
        return mapper.parseObject(response, LocationDto.class);
    }
}
