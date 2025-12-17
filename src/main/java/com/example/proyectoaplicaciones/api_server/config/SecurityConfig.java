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
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(authz -> authz
                        // --- INICIO DE LA CORRECCIÓN ---
                        // Regla 1: Permitir explícita y únicamente el acceso a los endpoints de autenticación.
                        .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        // Regla 2: Permitir la lectura (GET) de posts y comentarios a cualquiera.
                        .requestMatchers(HttpMethod.GET, "/api/posts/**", "/api/comments/**").permitAll()
                        // Regla 3: Proteger las rutas de borrado SÓLO para Admins.
                        .requestMatchers(HttpMethod.DELETE, "/api/posts/**", "/api/comments/**").hasRole(Role.ADMIN.name())
                        // Regla 4: CUALQUIER OTRA petición debe estar autenticada.
                        .anyRequest().authenticated()
                        // --- FIN DE LA CORRECCIÓN ---
                );
        return http.build();
    }
}
