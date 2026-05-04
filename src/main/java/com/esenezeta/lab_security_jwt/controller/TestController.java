package com.esenezeta.lab_security_jwt.controller;

import com.esenezeta.lab_security_jwt.service.JwtService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {

    private final JwtService jwtService;

    // Inyeccion por constructor: buena practica de arquitectura
    public TestController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hola esenenzeta, el ,muro te dejo pasar";
    }

    @GetMapping("/auth/token")
    public String getToken() {
        // Simulamos que el sistema te asigna el rol ADMIN al generar el token
        return jwtService.generateToken("esenezeta", List.of("ADMIN"));
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')") // Solo usuarios con ROLE_ADMIN pueden entrar
    public String admin() {
        return "Acceso concedido: Eres ADMIN de la plataforma esenezeta.";
    }
}