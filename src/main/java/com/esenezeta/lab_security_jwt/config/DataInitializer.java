package com.esenezeta.lab_security_jwt.config;

import com.esenezeta.lab_security_jwt.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UserRepository userRepository) {
        return args -> {
            // Verificamos la existencia del usuario maestro para confirmar conexion con RDS
            userRepository.findByUsername("esenezeta").ifPresentOrElse(
                    user -> System.out.println("**** INFRAESTRUCTURA : Conexion con PostgreSQL exitosa. Sistema Operativo. ****"),
                    () -> {
                        // Logica de inicializacion desactivada por seguridad.
                        // El usuario ya existe en la base de datos persistente.
                        System.out.println("**** INFRAESTRUCTURA : Advertencia - Usuario maestro no encontrado en la BD. ****");
                    }
            );
        };
    }
}