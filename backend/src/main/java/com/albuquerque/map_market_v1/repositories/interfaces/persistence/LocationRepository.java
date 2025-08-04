package com.albuquerque.map_market_v1.repositories.interfaces.persistence;

import com.albuquerque.map_market_v1.entities.LocationEntity;
import com.albuquerque.map_market_v1.entities.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {
    Optional<LocationEntity> findLocationByProductId(Long productId);
    List<LocationEntity> findByProductIsNotNull();

    @Query("""
        SELECT DISTINCT l.product
        FROM LocationEntity l
        WHERE l.product IS NOT NULL
    """)
    List<ProductEntity> findRegisteredProducts();
}
