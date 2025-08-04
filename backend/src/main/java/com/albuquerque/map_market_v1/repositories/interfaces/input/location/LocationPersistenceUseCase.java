package com.albuquerque.map_market_v1.repositories.interfaces.input.location;

public interface LocationPersistenceUseCase<Input, OutPut> {
    OutPut findById(Long id);
    OutPut findLocationByProductId(Long id);
    OutPut update(Input input);
}
