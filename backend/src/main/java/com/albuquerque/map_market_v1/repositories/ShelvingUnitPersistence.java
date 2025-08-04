package com.albuquerque.map_market_v1.repositories;

import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import com.albuquerque.map_market_v1.repositories.interfaces.input.PersistenceFindAllUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.PersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.persistence.ShelvingUnitRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ShelvingUnitPersistence implements
    PersistenceFindAllUseCase<ShelvingUnitEntity>,
    PersistenceUseCase<ShelvingUnitEntity, Optional<ShelvingUnitEntity>> {

    private final ShelvingUnitRepository repository;

    public ShelvingUnitPersistence(ShelvingUnitRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ShelvingUnitEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ShelvingUnitEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ShelvingUnitEntity> create(ShelvingUnitEntity entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public Optional<ShelvingUnitEntity> update(ShelvingUnitEntity data) {
        return Optional.of(repository.save(data));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    public boolean existResource(Long id) {
        return repository.existsById(id);
    }
}
