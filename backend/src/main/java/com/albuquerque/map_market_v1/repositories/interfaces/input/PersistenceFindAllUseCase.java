package com.albuquerque.map_market_v1.repositories.interfaces.input;

import java.util.List;

public interface PersistenceFindAllUseCase<OutPut> {
    List<OutPut> findAll();
}
