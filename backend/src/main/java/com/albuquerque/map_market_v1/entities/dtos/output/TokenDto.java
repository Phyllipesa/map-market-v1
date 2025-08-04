package com.albuquerque.map_market_v1.entities.dtos.output;

import java.io.Serial;
import java.io.Serializable;

public class TokenDto implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String accessToken;
    private String refreshToken;

    public TokenDto() {
    }

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
