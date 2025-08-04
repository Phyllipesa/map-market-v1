package com.albuquerque.map_market_v1.services;

import com.albuquerque.map_market_v1.entities.Token;
import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.entities.dtos.output.TokenDto;
import com.albuquerque.map_market_v1.exception.InvalidCredentialsException;
import com.albuquerque.map_market_v1.exception.messages.AuthErrorMessages;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.UserPersistence;
import com.albuquerque.map_market_v1.repositories.interfaces.input.auth.AuthServiceUseCase;
import com.albuquerque.map_market_v1.services.security.jwt.JwtTokenProvider;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements AuthServiceUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UserPersistence userPersistence;
    private final EntityMapper mapper;

    public AuthService(
        AuthenticationManager authenticationManager,
        JwtTokenProvider tokenProvider,
        UserPersistence userPersistence,
        EntityMapper mapper
    ) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userPersistence = userPersistence;
        this.mapper = mapper;
    }

    @Override
    public TokenDto signIn(AccountCredentialsDto data) {
        try
        {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    data.getUsername(),
                    data.getPassword()
                ));
        }
        catch (Exception e)
        {
            throw new InvalidCredentialsException(AuthErrorMessages.INVALID_USER_OR_PASSWORD);
        }

        Token tokenResponse;
        tokenResponse = tokenProvider.createAccessToken(data.getUsername());
        return mapper.parseObject(tokenResponse, TokenDto.class);
    }

    @Override
    public TokenDto refreshToken(String username, String refreshToken) {
        userPersistence.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(AuthErrorMessages.USERNAME_NOT_FOUND + username));
        Token tokenResponse;
        tokenResponse = tokenProvider.refreshToken(refreshToken);
        return mapper.parseObject(tokenResponse, TokenDto.class);
    }
}
