package com.uade.tpo.demo.entity.dto;

import lombok.Data;

@Data
public class ErrorResponseDTO {
    private boolean success;
    private String message;

    public ErrorResponseDTO() {}

    public ErrorResponseDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
