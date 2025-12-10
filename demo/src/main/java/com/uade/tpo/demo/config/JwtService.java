package com.uade.tpo.demo.config;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import com.uade.tpo.demo.entity.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

@Service
public class JwtService {

    // agarro la secret key y el tiempo de expiración del application.properties
    @Value("${application.security.jwt.secretKey}")
    private String secretKey;
    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    // genera el token a partir del usuario
    public String generateToken(
            User user) {
        return buildToken(user, jwtExpiration);
    }

    // construye el token a partir del usuario y un tiempo de expiración personalizado
    private String buildToken(
            User user,
            long expiration) {
        String roleName = null;
        try {
            roleName = user.getRole().name();
        } catch (ClassCastException ex) {
            var auths = user.getAuthorities();
            if (auths != null && !auths.isEmpty()) {
                String first = auths.iterator().next().getAuthority();
                roleName = first != null && first.startsWith("ROLE_") ? first.substring(5) : first;
            }
        }

        // construyo el token
        return Jwts
                .builder() // funcion hasheadora
                .subject(user.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration)) // timepo de expiración
                .claim("role", roleName) // guardo la info del tipo de rol
                .signWith(getSecretKey()) // firmo el token con la secret key
                .compact(); // lo convierto en String (el token)
    }

    // valida si el token es válido para el usuario
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractClaim(token, Claims::getSubject); // extraigo el username del token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }


// comprueba si el token está expirado
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    // extrae el username del token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // extrae cualquier claim del token usando una función pasada como parámetro
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // extrae todos los claims del token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // obtiene la clave secreta para firmar/verificar el token
    private SecretKey getSecretKey() {
        SecretKey secretKeySpec = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return secretKeySpec;
    }
}
