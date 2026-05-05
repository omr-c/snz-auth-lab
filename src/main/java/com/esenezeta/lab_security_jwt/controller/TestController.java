package com.esenezeta.lab_security_jwt.controller;

import com.esenezeta.lab_security_jwt.model.User;
import com.esenezeta.lab_security_jwt.repository.UserRepository;
import com.esenezeta.lab_security_jwt.service.JwtService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {

    private final JwtService jwtService;
    private final UserRepository userRepository; // Nueva inyeccion

    public TestController(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hola esenezeta, el muro te dejo pasar (Publico)";
    }

    @GetMapping("/auth/token")
    public String getToken(@RequestParam(defaultValue = "esenezeta") String username) {
        // Buscamos al usuario real en PostgreSQL[cite: 5]
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado en la BD"));

        // Generamos el token con los roles reales de la base de datos[cite: 4]
        return jwtService.generateToken(user.getUsername(), user.getRoles());
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Acceso concedido: Datos obtenidos de PostgreSQL para el ADMIN esenezeta.";
    }
}