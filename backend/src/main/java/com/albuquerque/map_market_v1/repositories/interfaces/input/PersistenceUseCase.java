package com.albuquerque.map_market_v1.repositories.interfaces.input;

public interface PersistenceUseCase<Input, OutPut> {
    OutPut findById(Long id);
    OutPut create(Input input);
    OutPut update(Input input);
    void delete(Long id);
}
