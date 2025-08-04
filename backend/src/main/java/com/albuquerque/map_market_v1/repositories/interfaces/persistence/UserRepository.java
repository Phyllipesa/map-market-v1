package com.albuquerque.map_market_v1.repositories.interfaces.persistence;

import com.albuquerque.map_market_v1.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(@Param("username") String username);
}