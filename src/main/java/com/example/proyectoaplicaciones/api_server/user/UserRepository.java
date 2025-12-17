package com.example.proyectoaplicaciones.api_server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    // --- INICIO DE LA CORRECCIÓN ---
    // Ambos métodos ahora devuelven Optional para un manejo de errores más seguro y consistente.
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    // --- FIN DE LA CORRECCIÓN ---
}
