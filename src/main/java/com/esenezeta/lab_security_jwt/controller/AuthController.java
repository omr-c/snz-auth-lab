package com.esenezeta.lab_security_jwt.controller;

import com.esenezeta.lab_security_jwt.model.User;
import com.esenezeta.lab_security_jwt.repository.UserRepository;
import com.esenezeta.lab_security_jwt.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Health Check consolidado para AWS Target Group en la raiz /
    @GetMapping("/")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "infrastructure", "Esenezeta SNZ",
                "message", "Servidor operacional y base de datos conectada"
        ));
    }

    @PostMapping("/auth/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("message", "El usuario ya existe"));
        }

        User newUser = new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                List.of("USER")
        );

        userRepository.save(newUser);
        return ResponseEntity.ok(Map.of("message", "Usuario registrado exitosamente en la infraestructura SNZ."));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
        return ResponseEntity.ok(Map.of("token", token));
    }
}