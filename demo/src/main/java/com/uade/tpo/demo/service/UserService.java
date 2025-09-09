package com.uade.tpo.demo.service;

import com.uade.tpo.demo.entity.User;
import com.uade.tpo.demo.entity.dto.UserRegisterRequest;
import com.uade.tpo.demo.entity.dto.UserLoginRequest;
import com.uade.tpo.demo.entity.dto.UserResponse;
import com.uade.tpo.demo.entity.dto.UserUpdateRequest;
import com.uade.tpo.demo.entity.dto.UserRoleUpdateRequest;

public interface UserService {
    
    UserResponse registerUser(UserRegisterRequest request);
    
    UserResponse loginUser(UserLoginRequest request);
    
    UserResponse getUserById(Long id);
    
    UserResponse updateUser(Long id, UserUpdateRequest request);
    
    void deleteUser(Long id);
    
    UserResponse updateUserRole(Long id, UserRoleUpdateRequest request);

    User getLoggedUser();
}
