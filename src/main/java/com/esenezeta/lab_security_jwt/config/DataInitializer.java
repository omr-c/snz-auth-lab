package com.esenezeta.lab_security_jwt.config;

import com.esenezeta.lab_security_jwt.model.User;
import com.esenezeta.lab_security_jwt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            userRepository.findByUsername("esenezeta").ifPresentOrElse(
                    user -> System.out.println("**** INFRAESTRUCTURA SNZ: Usuario listo en PostgreSQL ****"),
                    () -> {
                        User admin = new User("esenezeta", "password123", List.of("ADMIN"));
                        userRepository.save(admin);
                        System.out.println("**** INFRAESTRUCTURA SNZ: Datos maestros inicializados ****");
                    }
            );
        };
    }
}