package com.example.proyectoaplicaciones.api_server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional; // <-- Se añade la importación necesaria.

public interface UserRepository extends JpaRepository<User, Integer> {    // Este método puede que lo uses en otro sitio, lo dejamos como está.
    User findByUsername(String username);

    // --- INICIO DE LA CORRECCIÓN ---
    // Se cambia el tipo de retorno a Optional<User>.
    // Esto es crucial para que la nueva configuración de seguridad funcione
    // y soluciona el error "cannot find symbol: method map".
    Optional<User> findByEmail(String email);
    // --- FIN DE LA CORRECCIÓN ---
}
