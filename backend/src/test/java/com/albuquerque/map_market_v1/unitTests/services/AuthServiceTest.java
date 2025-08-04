package com.albuquerque.map_market_v1.unitTests.services;

import com.albuquerque.map_market_v1.entities.PermissionEntity;
import com.albuquerque.map_market_v1.entities.Token;
import com.albuquerque.map_market_v1.entities.UserEntity;
import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.entities.dtos.output.TokenDto;
import com.albuquerque.map_market_v1.mapper.EntityMapper;
import com.albuquerque.map_market_v1.repositories.UserPersistence;
import com.albuquerque.map_market_v1.services.AuthService;
import com.albuquerque.map_market_v1.services.security.jwt.JwtTokenProvider;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private UserPersistence userPersistence;

    @Mock
    private EntityMapper mapper;

    @Test
    void shouldReturnTokenWhenSignInWithValidCredentials() {
        // given
        String username = "user1";
        String password = "pass123";

        AccountCredentialsDto credentialDto = new AccountCredentialsDto(username, password);
        Token token = new Token(
            username,
            true,
            new Date(),
            new Date(System.currentTimeMillis() + 3600000),
            "accessToken",
            "refreshToken"
        );

        TokenDto expectedToken = new TokenDto("accessToken", "refreshToken");

        when(authenticationManager.authenticate(
            any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(tokenProvider.createAccessToken(username)).thenReturn(token);
        when(mapper.parseObject(token, TokenDto.class)).thenReturn(expectedToken);

        // when
        TokenDto result = authService.signIn(credentialDto);

        // then
        verify(authenticationManager).authenticate(
            argThat(auth -> auth.getName().equals(username) && auth.getCredentials().equals(password))
        );

        assertAll("SignIn: ",
            () -> assertNotNull(result),
            () -> assertEquals(expectedToken.getAccessToken(), result.getAccessToken()),
            () -> assertEquals(expectedToken.getRefreshToken(), result.getRefreshToken())
        );
    }

    @Test
    void shouldReturnNewAccessTokenWhenRefreshTokenWithValidUsername() {
        // given
        String username = "user1";
        String refreshToken = "refreshToken";

        PermissionEntity permissions = new PermissionEntity();
        List<PermissionEntity> roles = List.of(permissions);

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPermissions(roles);

        Token newToken = new Token(
            username,
            true,
            new Date(),
            new Date(System.currentTimeMillis() + 3600000),
            "accessToken",
            "refreshToken"
        );

        TokenDto expectedToken = new TokenDto("accessToken", "refreshToken");

        when(userPersistence.findByUsername(username)).thenReturn(Optional.of(userEntity));
        when(tokenProvider.refreshToken(refreshToken)).thenReturn(newToken);
        when(mapper.parseObject(newToken, TokenDto.class)).thenReturn(expectedToken);

        // when
        TokenDto result = authService.refreshToken(username, refreshToken);

        // then
        verify(userPersistence).findByUsername(username);
        verify(tokenProvider).refreshToken(refreshToken);
        verify(mapper).parseObject(newToken, TokenDto.class);

        assertAll("Refresh token: ",
            () -> assertNotNull(result),
            () -> assertEquals(expectedToken.getAccessToken(), result.getAccessToken()),
            () -> assertEquals(expectedToken.getRefreshToken(), result.getRefreshToken())
        );
    }
}
