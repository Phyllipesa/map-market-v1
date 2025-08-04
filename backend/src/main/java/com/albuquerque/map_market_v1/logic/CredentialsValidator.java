package com.albuquerque.map_market_v1.logic;

import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.exception.ParameterNotFoundException;
import com.albuquerque.map_market_v1.exception.messages.AuthErrorMessages;
import org.springframework.stereotype.Component;

@Component
public class CredentialsValidator {
    public void checkIfParamsIsNotNull(AccountCredentialsDto data) {
        if (data == null || data.getUsername() == null || data.getUsername().isBlank()
            || data.getPassword() == null || data.getPassword().isBlank())
            throw new ParameterNotFoundException(AuthErrorMessages.requiredCredentialsMessage("username/password"));
    }
}
