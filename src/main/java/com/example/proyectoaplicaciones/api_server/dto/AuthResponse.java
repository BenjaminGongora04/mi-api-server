package com.example.proyectoaplicaciones.api_server.dto;

import com.example.proyectoaplicaciones.api_server.user.Role;

public class AuthResponse {
    private String token;
    private Role role;

    public AuthResponse(String token, Role role) {
        this.token = token;
        this.role = role;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}