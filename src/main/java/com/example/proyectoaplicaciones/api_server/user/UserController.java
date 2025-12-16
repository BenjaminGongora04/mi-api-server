package com.example.proyectoaplicaciones.api_server.user;

// Se importan las clases que necesitamos
import com.example.proyectoaplicaciones.api_server.config.JwtService;
import com.example.proyectoaplicaciones.api_server.dto.AuthResponse;
import com.example.proyectoaplicaciones.api_server.dto.LoginRequest;
import com.example.proyectoaplicaciones.api_server.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService; // La fábrica de tokens

    // Inyectamos el JwtService en el constructor
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByUsername(registerRequest.getUsername()) != null || userRepository.findByEmail(registerRequest.getEmail()) != null) {
            return ResponseEntity.badRequest().build();
        }

        User newUser = new User();
        newUser.setUsername(registerRequest.getUsername());
        newUser.setEmail(registerRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setRole(Role.USER);

        User savedUser = userRepository.save(newUser);

        // Generamos el token y lo devolvemos
        String jwtToken = jwtService.generateToken(savedUser);
        AuthResponse authResponse = new AuthResponse(jwtToken, savedUser.getRole());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail());

        if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            // Generamos el token y lo devolvemos
            String jwtToken = jwtService.generateToken(user);
            AuthResponse authResponse = new AuthResponse(jwtToken, user.getRole());
            return ResponseEntity.ok(authResponse);
        }

        return ResponseEntity.status(401).build();
    }
}
