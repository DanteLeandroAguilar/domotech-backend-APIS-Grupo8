package com.uade.tpo.demo.entity.dto;

import com.uade.tpo.demo.entity.enums.Role;
import lombok.Data;

@Data
public class UserRoleUpdateRequest {
    private Role role;
}
