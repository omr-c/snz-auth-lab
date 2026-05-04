package com.esenezeta.lab_security_jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
    public class SecurityConfig {

        private final JwtAuthenticationFilter jwtAuthFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
            this.jwtAuthFilter = jwtAuthFilter;
        }
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            System.out.println("**** CONFIGURACION STATELESS DE ESENEZETA ACTIVADA ****");

            return http
                    .csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                            .requestMatchers("/hello").permitAll()
                            .requestMatchers("/auth/**").permitAll() // Liberamos la fabrica de tokens
                            .anyRequest().authenticated()
                    )

                    // 3. POLITICA STATELESS: Aqui esta la magia
                    .sessionManagement(session ->
                            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )

                    // AGREGAMOS NUESTRO FILTRO PERSONALIZADO AQUI:
                    .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                    .build();
        }
    }