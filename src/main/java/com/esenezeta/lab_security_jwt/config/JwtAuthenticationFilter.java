package com.esenezeta.lab_security_jwt.config;

import com.esenezeta.lab_security_jwt.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(7);

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(jwtService.getSecretKey())
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();

            String username = claims.getSubject();

            // --- INICIO DE LOGICA DE ROLES ---
            // Extraemos la lista de roles del claim "roles" que pusimos en el Token
            List<String> roles = claims.get("roles", List.class);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Convertimos cada rol (ej: "ADMIN") a un SimpleGrantedAuthority (ej: "ROLE_ADMIN")
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                // Creamos el token de autenticacion INCLUYENDO las autoridades (roles)
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        username,
                        null,
                        authorities
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("**** AUTH EXITOSA PARA: " + username + " CON ROLES: " + roles + " ****");
            }
            // --- FIN DE LOGICA DE ROLES ---

        } catch (Exception e) {
            System.out.println("**** ERROR DE VALIDACION JWT: " + e.getMessage() + " ****");
        }

        filterChain.doFilter(request, response);
    }
}