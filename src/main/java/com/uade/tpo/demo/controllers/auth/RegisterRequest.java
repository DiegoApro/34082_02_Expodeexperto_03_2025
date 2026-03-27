package com.uade.tpo.demo.controllers.auth;

import com.uade.tpo.demo.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String direccion;  // Nuevo
    private String telefono;   // Nuevo
    private Role role;
}