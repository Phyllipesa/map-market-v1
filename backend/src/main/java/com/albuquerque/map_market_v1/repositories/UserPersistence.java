package com.albuquerque.map_market_v1.repositories;

import com.albuquerque.map_market_v1.entities.UserEntity;
import com.albuquerque.map_market_v1.repositories.interfaces.input.user.UserPersistenceUseCase;
import com.albuquerque.map_market_v1.repositories.interfaces.persistence.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserPersistence implements UserPersistenceUseCase<UserEntity> {

    private final UserRepository repository;

    public UserPersistence(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return Optional.ofNullable(repository.findByUsername(username));
    }
}
