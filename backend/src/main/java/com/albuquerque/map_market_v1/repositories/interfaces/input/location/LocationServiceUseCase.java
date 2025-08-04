package com.albuquerque.map_market_v1.repositories.interfaces.input.location;

import java.util.List;

public interface LocationServiceUseCase<OutPut> {
    List<OutPut> findAll();
    OutPut findById(Long id);
    OutPut subscribingProduct(Long locationId, Long productId);
    OutPut unsubscribingProduct(Long locationId);
}
