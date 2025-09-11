package com.uade.tpo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.http.HttpMethod;
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
            .requestMatchers("/actuator/**").permitAll()  // Health checks
            
            // Categories: escritura solo SELLER, lectura pública
            .requestMatchers(POST,   "/categories/**").hasRole("SELLER")
            .requestMatchers(PUT,    "/categories/**").hasRole("SELLER")
            .requestMatchers(DELETE, "/categories/**").hasRole("SELLER")
            .requestMatchers(GET,    "/categories/**").permitAll()
            
            // === PRODUCTOS - LECTURA PÚBLICA (BUYER + SELLER + PÚBLICO) ===
            .requestMatchers(GET, "/api/products/catalog").permitAll()  // Catálogo público
            .requestMatchers(GET, "/api/products/search").permitAll()  // Búsqueda pública
            .requestMatchers(GET, "/api/products/filter").permitAll()  // Filtros unificados
            .requestMatchers(GET, "/api/products/filter/**").permitAll()  // Filtros legacy
            .requestMatchers(GET, "/api/products/category/**").permitAll()  // Por categoría
            .requestMatchers(GET, "/api/products/{id}").permitAll()  // Detalle producto
            .requestMatchers(GET, "/api/products/{id}/stock/**").permitAll()  // Verificar stock
            
            // === PRODUCTOS - GESTIÓN (SOLO SELLER) ===
            .requestMatchers(GET, "/api/products").hasRole("SELLER")  // Lista completa (admin view)
            .requestMatchers(POST, "/api/products").hasRole("SELLER")  // Crear producto
            .requestMatchers(PUT, "/api/products/**").hasRole("SELLER")  // Actualizar producto
            .requestMatchers(PATCH, "/api/products/*/stock").hasRole("SELLER")  // Actualizar stock
            .requestMatchers(PATCH, "/api/products/*/discount").hasRole("SELLER")  // Aplicar descuento
            .requestMatchers(DELETE, "/api/products/*/discount").hasRole("SELLER")  // Quitar descuento
            .requestMatchers(DELETE, "/api/products/**").hasRole("SELLER")  // Eliminar producto
            
            // === IMÁGENES - LECTURA PÚBLICA (BUYER + SELLER + PÚBLICO) ===
            .requestMatchers(GET, "/api/productos/*/images").permitAll()  // Ver todas las imágenes
            .requestMatchers(GET, "/api/productos/*/images/principal").permitAll()  // Ver imagen principal
            .requestMatchers(GET, "/api/images/*/download").permitAll()  // Descargar imagen
            .requestMatchers(GET, "/api/images/*").permitAll()  // Info de imagen

            // === IMÁGENES - GESTIÓN (SOLO SELLER) ===
            .requestMatchers(POST, "/api/productos/*/images").hasRole("SELLER")  // Subir imagen
            .requestMatchers(PUT, "/api/images/*/principal").hasRole("SELLER")  // Cambiar imagen principal
            .requestMatchers(DELETE, "/api/images/*").hasRole("SELLER")  // Eliminar imagen

            // === ORDERS - SOLO SELLER ===
            .requestMatchers(GET, "/orders").hasRole("SELLER")  // Filtrar órdenes con query params
            .requestMatchers(POST, "/orders").hasRole("BUYER")  // Crear orden (confirmar carrito)
            .requestMatchers(PUT, "/orders/me").authenticated()

            // Users: permisos específicos
            .requestMatchers(GET, "/users/**").authenticated()
            .requestMatchers(PUT, "/users/*/role").hasRole("SELLER")   // Solo SELLER puede cambiar roles
            .requestMatchers(DELETE, "/users/**").hasRole("SELLER")    // Solo SELLER puede eliminar usuarios
            .requestMatchers(PUT, "/users/**").hasRole("SELLER")       // Actualizar usuario (propio o SELLER)

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