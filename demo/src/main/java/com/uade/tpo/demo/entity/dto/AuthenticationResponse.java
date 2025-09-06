package com.uade.tpo.demo.entity.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    // DTO para devolver el token JWT tras autenticación
    @JsonProperty("access_token")
    private String accessToken;
}