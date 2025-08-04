package com.albuquerque.map_market_v1.repositories;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.repositories.interfaces.input.map.FindRegisteredProductsPersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.map.FindUnitsWithAssignedProductsPersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.map.MapMarketPersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.persistence.LocationRepository;

import com.albuquerque.map_market_v1.repositories.interfaces.persistence.ShelvingUnitRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MapMarketPersistence implements
    MapMarketPersistenceUseCase<LocationEntity>,
    FindRegisteredProductsPersistenceUseCase<ProductEntity>,
    FindUnitsWithAssignedProductsPersistenceUseCase<ShelvingUnitEntity> {

    private final LocationRepository repository;
    private final ShelvingUnitRepository shelvingUnitRepository;

    public MapMarketPersistence(LocationRepository repository, ShelvingUnitRepository shelvingUnitRepository) {
        this.repository = repository;
        this.shelvingUnitRepository = shelvingUnitRepository;
    }

    @Override
    public List<ProductEntity> findRegisteredProducts() {
        return repository.findRegisteredProducts();
    }

    @Override
    public List<LocationEntity> findByProductIsNotNull() {
        return repository.findByProductIsNotNull();
    }

    @Override
    public Optional<LocationEntity> findLocationByProductId(Long id) {
        return repository.findLocationByProductId(id);
    }

    @Override
    public List<ShelvingUnitEntity> findUnitsWithAssignedProducts() {
        return shelvingUnitRepository.findUnitsWithAssignedProducts();
    }
}
