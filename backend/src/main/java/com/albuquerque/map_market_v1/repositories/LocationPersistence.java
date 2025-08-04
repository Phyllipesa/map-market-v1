package com.albuquerque.map_market_v1.repositories;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.repositories.interfaces.input.location.LocationPersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.PersistenceFindAllUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.persistence.LocationRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class LocationPersistence implements
    PersistenceFindAllUseCase<LocationEntity>,
    LocationPersistenceUseCase<LocationEntity, Optional<LocationEntity>> {

    private final LocationRepository repository;

    public LocationPersistence(LocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<LocationEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<LocationEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<LocationEntity> findLocationByProductId(Long id) {
        return repository.findLocationByProductId(id);
    }

    @Override
    public Optional<LocationEntity> update(LocationEntity locationEntity) {
        return Optional.of(repository.save(locationEntity));
    }
}
