package com.esenezeta.lab_security_jwt.controller;

import com.esenezeta.lab_security_jwt.model.User;
import com.esenezeta.lab_security_jwt.repository.UserRepository;
import com.esenezeta.lab_security_jwt.service.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/auth")
// NO usar @CrossOrigin aqui, ya esta configurado globalmente en SecurityConfig
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("El usuario ya existe");
        }
        User newUser = new User(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                List.of("USER")
        );
        userRepository.save(newUser);
        return "Usuario registrado exitosamente en la infraestructura SNZ.";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Credenciales invalidas"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Credenciales invalidas");
        }

        String token = jwtService.generateToken(user.getUsername(), user.getRoles());
        return Map.of("token", token);
    }
}