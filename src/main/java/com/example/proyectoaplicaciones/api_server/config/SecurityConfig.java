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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authz -> authz
                        // --- INICIO DE LA CORRECCIÓN DE DIAGNÓSTICO ---
                        // Esta regla DESACTIVA toda la seguridad. Le dice a Spring
                        // que PERMITA CUALQUIER PETICIÓN a CUALQUIER RUTA.
                        // Es temporal y solo para encontrar la causa del Error 403.
                        .anyRequest().permitAll()
                        // --- FIN DE LA CORRECCIÓN DE DIAGNÓSTICO ---
                );

        return http.build();
    }
}
