package com.example.proyectoaplicaciones.api_server.user;

/**
 * Enum para definir los roles de usuario en la aplicación.
 * Usar un Enum es más seguro y robusto que usar Strings.
 */
public enum Role {
    USER,       // Rol estándar para usuarios registrados.
    MODERATOR,  // Rol para usuarios con permisos para borrar contenido.
    ADMIN       // Rol con control total.
}