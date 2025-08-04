package com.albuquerque.map_market_v1.repositories.interfaces.input.user;

import java.util.Optional;

public interface UserPersistenceUseCase<T> {
    Optional<T> findByUsername(String userName);
}
