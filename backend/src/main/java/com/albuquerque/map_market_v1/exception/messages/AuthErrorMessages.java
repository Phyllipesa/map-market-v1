package com.albuquerque.map_market_v1.exception.messages;

public class AuthErrorMessages {
    public static final String INVALID_USER_OR_PASSWORD = "Invalid username/password supplied!";
    public static final String EXPIRED_OR_INVALID_JWT_TOKEN = "Expired or invalid JWT token!";
    public static final String USERNAME_NOT_FOUND = "Username not found: ";

    public static String requiredCredentialsMessage(String info) {
        return "Credentials '" + info + "' is null or blank!";
    }
}
