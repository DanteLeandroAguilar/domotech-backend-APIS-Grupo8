package com.uade.tpo.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                // === ENDPOINTS PÚBLICOS (sin autenticación) ===
                .requestMatchers("/users/**").permitAll()  // Registro y login
                .requestMatchers("/actuator/**").permitAll()  // Health checks
                
                // === PRODUCTOS - LECTURA PÚBLICA (BUYER + SELLER + PÚBLICO) ===
                .requestMatchers(HttpMethod.GET, "/api/products/catalog").permitAll()  // Catálogo público
                .requestMatchers(HttpMethod.GET, "/api/products/search").permitAll()  // Búsqueda pública
                .requestMatchers(HttpMethod.GET, "/api/products/category/**").permitAll()  // Por categoría
                .requestMatchers(HttpMethod.GET, "/api/products/filter/**").permitAll()  // Filtros
                .requestMatchers(HttpMethod.GET, "/api/products/{id}").permitAll()  // Detalle producto
                .requestMatchers(HttpMethod.GET, "/api/products/{id}/stock/**").permitAll()  // Verificar stock
                
                // === PRODUCTOS - GESTIÓN (SOLO SELLER) ===
                .requestMatchers(HttpMethod.POST, "/api/products").hasRole("SELLER")  // Crear producto
                .requestMatchers(HttpMethod.PUT, "/api/products/**").hasRole("SELLER")  // Actualizar producto
                .requestMatchers(HttpMethod.PATCH, "/api/products/*/stock").hasRole("SELLER")  // Actualizar stock
                .requestMatchers(HttpMethod.PATCH, "/api/products/*/discount").hasRole("SELLER")  // Aplicar descuento
                .requestMatchers(HttpMethod.DELETE, "/api/products/*/discount").hasRole("SELLER")  // Quitar descuento
                .requestMatchers(HttpMethod.DELETE, "/api/products/**").hasRole("SELLER")  // Eliminar producto
                
                // === IMÁGENES - LECTURA PÚBLICA (BUYER + SELLER + PÚBLICO) ===
                .requestMatchers(HttpMethod.GET, "/api/productos/*/images").permitAll()  // Ver todas las imágenes
                .requestMatchers(HttpMethod.GET, "/api/productos/*/images/principal").permitAll()  // Ver imagen principal
                .requestMatchers(HttpMethod.GET, "/api/images/*/download").permitAll()  // Descargar imagen
                .requestMatchers(HttpMethod.GET, "/api/images/*").permitAll()  // Info de imagen
                
                // === IMÁGENES - GESTIÓN (SOLO SELLER) ===
                .requestMatchers(HttpMethod.POST, "/api/productos/*/images").hasRole("SELLER")  // Subir imagen
                .requestMatchers(HttpMethod.PUT, "/api/images/*/principal").hasRole("SELLER")  // Cambiar imagen principal
                .requestMatchers(HttpMethod.DELETE, "/api/images/*").hasRole("SELLER")  // Eliminar imagen
                
                // === OTROS MÓDULOS DEL EQUIPO ===
                .requestMatchers("/api/categories/**").permitAll()  // Categories (otro compañero)
                .requestMatchers("/api/cart/**").hasRole("BUYER")  // Cart (compradores)
                .requestMatchers("/api/orders/**").hasAnyRole("BUYER", "SELLER")  // Orders (ambos)
                
                // === FALLBACK ===
                .anyRequest().authenticated()  // Cualquier otro endpoint requiere autenticación
            );
        
        return http.build();
    }
}