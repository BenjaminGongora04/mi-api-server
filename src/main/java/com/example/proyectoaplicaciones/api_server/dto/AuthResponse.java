package com.example.proyectoaplicaciones.api_server.dto;

import com.example.proyectoaplicaciones.api_server.user.Role;

public class AuthResponse {
    private String token;
    private Integer id;
    private String username;
    private String email;
    private Role role;

    // --- INICIO DE LA CORRECCIÓN ---
    // El constructor ahora acepta los 5 campos que el UserController le envía.
    // Esto solucionará el error de compilación "actual and formal argument lists differ in length".
    public AuthResponse(String token, Integer id, String username, String email, Role role) {
        this.token = token;
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }
    // --- FIN DE LA CORRECCIÓN ---

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
