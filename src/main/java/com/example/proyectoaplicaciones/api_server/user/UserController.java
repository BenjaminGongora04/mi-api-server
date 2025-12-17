package com.example.proyectoaplicaciones.api_server.user;

import com.example.proyectoaplicaciones.api_server.config.JwtService;
import com.example.proyectoaplicaciones.api_server.dto.AuthResponse;
import com.example.proyectoaplicaciones.api_server.dto.LoginRequest;
import com.example.proyectoaplicaciones.api_server.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        // --- INICIO DE LA CORRECCIÓN FINAL ---
        // 1. Comprobar si el nombre de usuario ya existe.
        if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso.");
        }

        // 2. Comprobar si el email ya existe.
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: El email ya está en uso.");
        }
        // --- FIN DE LA CORRECCIÓN FINAL ---

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(Role.USER);
        userRepository.save(newUser);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(newUser.getEmail())
                .password(newUser.getPassword())
                .roles(newUser.getRole().name())
                .build();

        String jwtToken = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(jwtToken, newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String jwtToken = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(jwtToken, user.getId(), user.getUsername(), user.getEmail(), user.getRole());
        return ResponseEntity.ok(authResponse);
    }
}
