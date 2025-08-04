package com.albuquerque.map_market_v1.repositories.interfaces.input;

public interface ServicePatchUseCase<Input, OutPut> {
    OutPut patch(Long id, Input input);
}
