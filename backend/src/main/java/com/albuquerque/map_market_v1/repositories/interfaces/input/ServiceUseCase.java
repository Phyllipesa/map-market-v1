package com.albuquerque.map_market_v1.repositories.interfaces.input;

import java.util.List;

public interface ServiceUseCase<Input, OutPut> {
    List<OutPut> findAll();
    OutPut findById(Long id);
    OutPut create(Input input);
    OutPut update(Long id, Input input);
    void delete(Long id);
}
