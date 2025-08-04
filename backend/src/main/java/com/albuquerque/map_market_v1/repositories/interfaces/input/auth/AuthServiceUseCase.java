package com.albuquerque.map_market_v1.repositories.interfaces.input.auth;

import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.entities.dtos.output.TokenDto;

public interface AuthServiceUseCase {
    TokenDto signIn(AccountCredentialsDto data);
    TokenDto refreshToken(String username, String refreshToken);
}
