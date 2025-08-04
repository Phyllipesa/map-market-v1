package com.albuquerque.map_market_v1.services;

import com.albuquerque.map_market_v1.exception.messages.AuthErrorMessages;
import com.albuquerque.map_market_v1.repositories.UserPersistence;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserPersistence persistence;

    public UserService(UserPersistence persistence) {
        this.persistence = persistence;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return persistence.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(AuthErrorMessages.USERNAME_NOT_FOUND + username));
    }
}
