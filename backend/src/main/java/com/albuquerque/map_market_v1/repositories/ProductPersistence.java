package com.albuquerque.map_market_v1.repositories;

import com.albuquerque.map_market_v1.entities.ProductEntity;
import com.albuquerque.map_market_v1.repositories.interfaces.input.PersistenceFindAllUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.input.PersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.persistence.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ProductPersistence implements
    PersistenceFindAllUseCase<ProductEntity>,
    PersistenceUseCase<ProductEntity, Optional<ProductEntity>>
{
    private final ProductRepository repository;

    public ProductPersistence(ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductEntity> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ProductEntity> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ProductEntity> create(ProductEntity entity) {
        return Optional.of(repository.save(entity));
    }

    @Override
    public Optional<ProductEntity> update(ProductEntity data) {
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
