package com.albuquerque.map_market_v1.logic;

import com.albuquerque.map_market_v1.exception.ParameterNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.AuthErrorMessages;
import org.springframework.stereotype.Component;

@Component
public class RefreshCredentialsValidator {
    public void checkIfParamsIsNotNull(String username, String refreshToken) {
        if(refreshToken == null || refreshToken.isBlank() || username == null || username.isBlank())
            throw new ParameterNotFoundException(AuthErrorMessages.requiredCredentialsMessage("username/token"));
    }
}
