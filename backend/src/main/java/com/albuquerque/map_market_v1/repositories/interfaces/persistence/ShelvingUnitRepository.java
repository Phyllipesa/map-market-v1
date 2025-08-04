package com.albuquerque.map_market_v1.repositories.interfaces.persistence;

import com.albuquerque.map_market_v1.entities.ShelvingUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShelvingUnitRepository extends JpaRepository<ShelvingUnitEntity, Long> {
    @Query("""
        SELECT DISTINCT su
        FROM ShelvingUnitEntity su
        JOIN LocationEntity l ON su.id = l.shelvingUnit.id
        WHERE l.product IS NOT NULL
    """)
    List<ShelvingUnitEntity> findUnitsWithAssignedProducts();
}
