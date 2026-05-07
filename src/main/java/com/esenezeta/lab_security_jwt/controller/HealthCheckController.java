package com.esenezeta.lab_security_jwt.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthCheckController {

    @GetMapping("/")
    public Map<String, String> healthCheck() {
        // Respuesta simple para indicar que el contexto de Spring ha cargado correctamente
        return Map.of(
                "status", "UP",
                "infrastructure", "Esenezeta SNZ",
                "message", "Servidor operacional"
        );
    }
}