package com.esenezeta.lab_security_jwt.config;

import com.esenezeta.lab_security_jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    // Usamos la misma llave que en el servicio
    private static final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
            "esta_es_una_llave_secreta_muy_larga_y_segura_para_el_laboratorio_snz".getBytes()
    );

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Extraer el encabezado Authorization
        String authHeader = request.getHeader("Authorization");

        // 2. Validar que tenga el formato "Bearer <token>"
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7); // Extraemos solo el token

        try {
            // 3. Validar y parsear el Token
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            String username = claims.getSubject();

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 4. Crear la autenticacion para Spring Security
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username, null, Collections.emptyList()
                );

                // 5. Establecer la seguridad en el contexto
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("**** USUARIO AUTENTICADO POR JWT: " + username + " ****");
            }

        } catch (Exception e) {
            System.out.println("**** ERROR DE JWT: " + e.getMessage() + " ****");
        }

        // 6. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}