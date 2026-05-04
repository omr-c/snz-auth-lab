package com.esenezeta.lab_security_jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello").permitAll() // La puerta abierta para tu prueba
                        .anyRequest().authenticated()          // Todo lo demás sigue bajo llave
                )
                .formLogin(conf -> conf.permitAll())       // Mantenemos el formulario por ahora
                .build();                                  // Construimos la cadena
    }
}