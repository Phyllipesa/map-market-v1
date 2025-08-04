package com.albuquerque.map_market_v1.repositories.interfaces.input.map;

import java.util.List;
import java.util.Optional;

public interface MapMarketPersistenceUseCase<OutPut> {
    List<OutPut> findByProductIsNotNull();
    Optional<OutPut> findLocationByProductId(Long id);
}
