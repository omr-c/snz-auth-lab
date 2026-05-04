package com.esenezeta.lab_security_jwt.controller;

import com.esenezeta.lab_security_jwt.service.JwtService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
// Generamos un token para tu perfil profesional
        return jwtService.generateToken("esenezeta");
    }
    @GetMapping("/admin")
    public String admin() {
        return "Bienvenido a la zona VIP, esenezeta. Tu token es valido.";
    }
}