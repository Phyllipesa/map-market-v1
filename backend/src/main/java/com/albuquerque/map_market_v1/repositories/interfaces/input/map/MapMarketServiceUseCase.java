package com.albuquerque.map_market_v1.repositories.interfaces.input.map;

import java.util.List;

public interface MapMarketServiceUseCase<OutPut> {
    List<OutPut> findByProductIsNotNull();
    OutPut findLocationByProductId(Long id);
}
