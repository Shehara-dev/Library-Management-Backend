package com.library.librarymanagement.dto;

import com.library.librarymanagement.entity.User.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private Role role;
}