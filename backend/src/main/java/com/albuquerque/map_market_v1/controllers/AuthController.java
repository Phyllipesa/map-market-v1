package com.albuquerque.map_market_v1.controllers;

import com.albuquerque.map_market_v1.entities.dtos.output.AccountCredentialsDto;
import com.albuquerque.map_market_v1.entities.dtos.output.TokenDto;
import com.albuquerque.map_market_v1.logic.CredentialsValidator;
import com.albuquerque.map_market_v1.logic.RefreshCredentialsValidator;
import com.albuquerque.map_market_v1.services.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final CredentialsValidator credentialsValidator;
    private final RefreshCredentialsValidator refreshCredentialsValidator;

    public AuthController(
        AuthService authService,
        CredentialsValidator credentialsValidator,
        RefreshCredentialsValidator refreshCredentialsValidator
    ) {
        this.authService = authService;
        this.credentialsValidator = credentialsValidator;
        this.refreshCredentialsValidator = refreshCredentialsValidator;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenDto> signIn(@RequestBody AccountCredentialsDto data) {
        credentialsValidator.checkIfParamsIsNotNull(data);
        return ResponseEntity.ok(authService.signIn(data));
    }

    @PutMapping("/refresh/{username}")
    public ResponseEntity<TokenDto> refreshToken(
        @PathVariable("username") String username,
        @RequestHeader("Authorization") String refreshToken
    ) {
        refreshCredentialsValidator.checkIfParamsIsNotNull(username, refreshToken);
        return ResponseEntity.ok(authService.refreshToken(username, refreshToken));
    }
}
