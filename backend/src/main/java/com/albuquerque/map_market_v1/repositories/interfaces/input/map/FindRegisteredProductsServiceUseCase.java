package com.albuquerque.map_market_v1.repositories.interfaces.input.map;

import java.util.List;

public interface FindRegisteredProductsServiceUseCase<OutPut> {
    List<OutPut> findRegisteredProducts();
}
