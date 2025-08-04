package com.albuquerque.map_market_v1.services.security.jwt;

import com.albuquerque.map_market_v1.entities.Token;
import com.albuquerque.map_market_v1.exception.InvalidJwtAuthenticationException;
import com.albuquerque.map_market_v1.exception.messages.AuthErrorMessages;

import com.albuquerque.map_market_v1.services.UserService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds; // 1h

    private final UserService userService;

    private Algorithm algorithm;

    private static final List<String> ADMIN_ROLES = List.of("ROLE_ADMIN");

    private static final long REFRESH_TOKEN_MULTIPLIER = 3;

    public JwtTokenProvider(UserService userService) {
        this.userService = userService;
    }

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public Token createAccessToken(String username) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        Date refreshValidity = new Date(now.getTime() + validityInMilliseconds * REFRESH_TOKEN_MULTIPLIER);

        var accessToken = generateToken(username, now, validity, issuerUrl);
        var refreshToken = generateToken(username, now, refreshValidity,null);
        return new Token(username, true, now, validity, accessToken, refreshToken);
    }

    public Token refreshToken(String refreshToken) {
        try
        {
            if (refreshToken.startsWith("Bearer "))
                refreshToken = refreshToken.substring("Bearer ".length());

            DecodedJWT decodedJWT = decodedToken(refreshToken);

            String username = decodedJWT.getSubject();
            return createAccessToken(username);
        }
        catch (JWTVerificationException e)
        {
            throw new InvalidJwtAuthenticationException(AuthErrorMessages.EXPIRED_OR_INVALID_JWT_TOKEN);
        }
    }

    private String generateToken(String username, Date issuedAt, Date expiresAt, String issuer) {
        JWTCreator.Builder builder = JWT.create()
            .withClaim("authorities", ADMIN_ROLES)
            .withIssuedAt(issuedAt)
            .withExpiresAt(expiresAt)
            .withSubject(username);

        if (issuer != null) {
            builder.withIssuer(issuer);
        }

        return builder.sign(algorithm);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = decodedToken(token);
        UserDetails userDetails = userService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private DecodedJWT decodedToken(String token) {
        try
        {
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(token);
        }
        catch (JWTVerificationException e)
        {
            throw new InvalidJwtAuthenticationException(AuthErrorMessages.EXPIRED_OR_INVALID_JWT_TOKEN);
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length());
        }
        return null;
    }

    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new InvalidJwtAuthenticationException(AuthErrorMessages.EXPIRED_OR_INVALID_JWT_TOKEN);
        }
        DecodedJWT decodedJWT = decodedToken(token);
        return !decodedJWT.getExpiresAt().before(new Date());
    }
}
