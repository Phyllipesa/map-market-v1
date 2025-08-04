package com.albuquerque.map_market_v1.repositories.interfaces.persistence;

import com.albuquerque.map_market_v1.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
