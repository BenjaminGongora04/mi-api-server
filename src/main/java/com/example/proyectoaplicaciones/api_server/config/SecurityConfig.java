package com.example.proyectoaplicaciones.api_server.config;

import com.example.proyectoaplicaciones.api_server.user.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desactivar CSRF para APIs sin estado
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // No crear sesiones
                .authenticationProvider(authenticationProvider) // Usar nuestro proveedor de autenticación personalizado
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Añadir nuestro filtro de JWT
                .authorizeHttpRequests(authz -> authz
                        // Regla 1: Los endpoints de autenticación son PÚBLICOS para todos.
                        .requestMatchers("/api/auth/**").permitAll()
                        // Regla 2: La lectura (GET) de posts y comentarios es PÚBLICA.
                        .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/comments/**").permitAll()
                        // Regla 3: El borrado (DELETE) de posts y comentarios requiere el rol de ADMIN.
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**", "/api/comments/**").hasRole(Role.ADMIN.name())
                        // Regla 4: CUALQUIER OTRA petición requiere que el usuario esté autenticado.
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}

