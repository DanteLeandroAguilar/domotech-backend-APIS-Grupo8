package com.uade.tpo.demo.entity.dto;

import com.uade.tpo.demo.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    // DTO para recibir datos de registro de usuario
    private String username;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;
}