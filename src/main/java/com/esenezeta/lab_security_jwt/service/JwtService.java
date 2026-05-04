package com.esenezeta.lab_security_jwt.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    // Generamos una llave segura para el algoritmo HS256
    // NOTA: En produccion, esto debe ser una cadena cargada desde variables de entorno
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "esta_es_una_llave_secreta_muy_larga_y_segura_para_el_laboratorio_snz".getBytes()
    );

    // Tiempo de vida del token: 1 hora
    private static final long EXPIRATION_TIME = 3600000;

    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        claims.put("author", "esenezeta");

        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }
}