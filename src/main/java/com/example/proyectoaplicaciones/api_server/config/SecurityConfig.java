package com.example.proyectoaplicaciones.api_server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // NOTA: Hemos eliminado todas las inyecciones y configuraciones complejas para esta prueba.

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactivar CSRF sigue siendo una buena práctica
                .authorizeHttpRequests(authz -> authz
                        // --- INICIO DE LA CORRECCIÓN DE DIAGNÓSTICO ---
                        // Esta es la única regla. Le dice a Spring que
                        // PERMITA CUALQUIER PETICIÓN a CUALQUIER RUTA sin hacer preguntas.
                        .anyRequest().permitAll()
                        // --- FIN DE LA CORRECCIÓN DE DIAGNÓSTICO ---
                );

        return http.build();
    }
}


