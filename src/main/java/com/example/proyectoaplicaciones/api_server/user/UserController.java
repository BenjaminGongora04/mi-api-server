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
    private final AuthenticationManager authenticationManager; // Se añade el AuthenticationManager

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager; // Se inicializa
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null || userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(Role.USER);

        userRepository.save(newUser);

        // Se crea el UserDetails para generar el token
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(newUser.getEmail())
                .password(newUser.getPassword())
                .roles(newUser.getRole().name())
                .build();

        String jwtToken = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(jwtToken, newUser.getId(), newUser.getUsername(), newUser.getEmail(), newUser.getRole());

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        // --- INICIO DE LA CORRECCIÓN ---
        // Se utiliza el AuthenticationManager de Spring para validar las credenciales.
        // Esto es más seguro y estándar.
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(),
                loginRequest.getPassword()
            )
        );

        // Si la autenticación es exitosa, buscamos al usuario (que sabemos que existe)
        // y generamos el token.
        User user = userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        
        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();

        String jwtToken = jwtService.generateToken(userDetails);
        AuthResponse authResponse = new AuthResponse(jwtToken, user.getId(), user.getUsername(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(authResponse);
        // --- FIN DE LA CORRECCIÓN ---
    }
}
