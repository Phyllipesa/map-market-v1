package com.albuquerque.map_market_v1.services.security.config;

import com.albuquerque.map_market_v1.services.UserService;
import com.albuquerque.map_market_v1.services.security.jwt.JwtTokenFilter;
import com.albuquerque.map_market_v1.services.security.jwt.JwtTokenProvider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final String ADMIN = "ADMIN";

    @Bean
    PasswordEncoder passwordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        Pbkdf2PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder(
            "",
            8,
            185000,
            Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256
        );
        encoders.put("pbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, JwtTokenProvider tokenProvider) throws Exception {
        JwtTokenFilter customFilter = new JwtTokenFilter(tokenProvider);

        return http
            .httpBasic(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/auth/sign-in", "/auth/refresh/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/map/**").permitAll()

                // Admin-only
                .requestMatchers(HttpMethod.GET,
                    "/location",
                    "/location/**",
                    "/product",
                    "/product/**",
                    "/shelving-unit",
                    "/shelving-unit/**").hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT, "/location/{locationId}/product/{productId}", "/location/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.POST, "/product", "/shelving-unit").hasRole(ADMIN)
                .requestMatchers(HttpMethod.PUT, "/product/{id}", "/shelving-unit/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.PATCH, "/shelving-unit/{id}").hasRole(ADMIN)
                .requestMatchers(HttpMethod.DELETE, "/product/{id}", "/shelving-unit/{id}").hasRole(ADMIN)

                // Explicit denial
                .requestMatchers("/users").denyAll()

                // All others must be authenticated
                .anyRequest().authenticated()
            )
            .build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    JwtTokenFilter jwtTokenFilter(JwtTokenProvider jwtTokenProvider) {
        return new JwtTokenFilter(jwtTokenProvider);
    }

    @Bean
    JwtTokenProvider jwtTokenProvider(UserService userService) {
        return new JwtTokenProvider(userService);
    }
}
