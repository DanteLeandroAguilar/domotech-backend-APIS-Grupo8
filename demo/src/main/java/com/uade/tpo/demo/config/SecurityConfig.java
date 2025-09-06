package com.uade.tpo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static org.springframework.http.HttpMethod.*;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;
        private final AuthenticationProvider authenticationProvider;
        private final SecurityExceptionHandler accessDeniedHandler;
        private final SecurityAuthenticationEntryPoint authenticationEntryPoint;

@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(req -> req
            // Público
            .requestMatchers("/auth/**").permitAll()

            // Categories: escritura solo SELLER, lectura pública
            .requestMatchers(POST,   "/categories/**").hasRole("SELLER")
            .requestMatchers(PUT,    "/categories/**").hasRole("SELLER")
            .requestMatchers(DELETE, "/categories/**").hasRole("SELLER")
            .requestMatchers(GET,    "/categories/**").permitAll()

            // Carts: solo BUYER
            .requestMatchers("/carts/**").hasRole("BUYER")

            // Imágenes: lectura pública, escritura SELLER
            .requestMatchers(GET,  "/api/productos/*/images/**", "/api/images/**").permitAll()
            .requestMatchers(POST, "/api/productos/*/images").hasRole("SELLER")
            .requestMatchers(PUT,  "/api/images/**").hasRole("SELLER")
            .requestMatchers(DELETE, "/api/images/**").hasRole("SELLER")

            // Users: autenticado
            .requestMatchers("/users/**").authenticated()

            // Todo lo demás, autenticado
            .anyRequest().authenticated()
        )
        .sessionManagement(s -> s.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling(ex -> ex
            .accessDeniedHandler(accessDeniedHandler)
            .authenticationEntryPoint(authenticationEntryPoint)
        );

    return http.build();
}
}
