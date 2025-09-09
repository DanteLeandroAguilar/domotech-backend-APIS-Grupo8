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
                // Endpoints públicos (sin autenticación)
                .requestMatchers("/users/**").permitAll()  // Registro y login
                .requestMatchers("/actuator/**").permitAll()  // Health checks
                
                // Imágenes - Lectura pública
                .requestMatchers(HttpMethod.GET, "/api/productos/*/images").permitAll()  // Ver lista de imágenes
                .requestMatchers(HttpMethod.GET, "/api/productos/*/images/principal").permitAll()  // Ver imagen principal
                .requestMatchers(HttpMethod.GET, "/api/images/*/download").permitAll()  // Descargar imágenes
                .requestMatchers(HttpMethod.GET, "/api/images/*").permitAll()  // Info de imagen
                
                // Imágenes - Escritura solo SELLER
                .requestMatchers(HttpMethod.POST, "/api/productos/*/images").permitAll() //.hasRole("SELLER")  // Subir imagen
                .requestMatchers(HttpMethod.PUT, "/api/images/*/principal").permitAll() //.hasRole("SELLER")  // Cambiar principal
                .requestMatchers(HttpMethod.DELETE, "/api/images/*").permitAll() //.hasRole("SELLER")  // Eliminar imagen
                
                // Otros endpoints - no son tu responsabilidad
                .requestMatchers("/api/**").permitAll()  // Resto de APIs (categories, products, etc.)
                
                // Todo lo demás requiere autenticación
                .anyRequest().authenticated()
            );
        
        return http.build();
    }
}