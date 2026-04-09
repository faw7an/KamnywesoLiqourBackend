package com.backend.kamnywesoliqourbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF (Cross-Site Request Forgery)
                // We disable this because we are building a stateless API
                .csrf(csrf -> csrf.disable())

                // 2. Authorize all requests
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // This "unlocks" all your endpoints
                );

        return http.build();
    }
}