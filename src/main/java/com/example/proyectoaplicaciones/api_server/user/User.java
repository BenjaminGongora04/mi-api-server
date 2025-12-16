package com.example.proyectoaplicaciones.api_server.user;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String username;
    private String email;
    private String password;

    // --- INICIO DE LA CORRECCIÓN ---
    @Enumerated(EnumType.STRING) // Le dice a JPA que guarde el nombre del rol (ej. "USER") como un String en la BD.
    private Role role;
    // --- FIN DE LA CORRECCIÓN ---

    // Constructor vacío
    public User() {
    }

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // --- INICIO DE LA CORRECCIÓN ---
    // Getter y Setter para el nuevo campo 'role'.
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    // --- FIN DE LA CORRECCIÓN ---
}
