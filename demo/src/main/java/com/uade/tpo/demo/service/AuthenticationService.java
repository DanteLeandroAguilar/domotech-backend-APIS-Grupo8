package com.uade.tpo.demo.service;

import com.uade.tpo.demo.controllers.auth.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uade.tpo.demo.controllers.auth.AuthenticationRequest;
import com.uade.tpo.demo.controllers.auth.RegisterRequest;
import com.uade.tpo.demo.config.JwtService;
import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.repository.UserRepository;
import com.uade.tpo.demo.exceptions.UserAlreadyExistsException;
import com.uade.tpo.demo.exceptions.InvalidInputException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                // Validaciones de campos obligatorios
                if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                        throw new InvalidInputException("El username es obligatorio");
                }
                if (request.getFirstname() == null || request.getFirstname().trim().isEmpty()) {
                        throw new InvalidInputException("El nombre es obligatorio");
                }
                if (request.getLastname() == null || request.getLastname().trim().isEmpty()) {
                        throw new InvalidInputException("El apellido es obligatorio");
                }
                if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                        throw new InvalidInputException("El email es obligatorio");
                }
                if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                        throw new InvalidInputException("La contraseña es obligatoria");
                }

                // Validaciones de unicidad previas al guardado para evitar 500
                if (repository.existsByEmail(request.getEmail())) {
                        throw new UserAlreadyExistsException("El email ya está registrado");
                }
                if (repository.existsByUsername(request.getUsername())) {
                        throw new UserAlreadyExistsException("El username ya está registrado");
                }
                var user = User.builder()
                                .username(request.getUsername())
                                .name(request.getFirstname())
                                .lastName(request.getLastname())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .role(request.getRole() != null ? request.getRole() : com.uade.tpo.demo.entity.enums.Role.BUYER)
                                .registrationDate(new java.util.Date())
                                .build();

                repository.save(user);
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));

                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtService.generateToken(user);
                return AuthenticationResponse.builder()
                                .accessToken(jwtToken)
                                .build();
        }
}
