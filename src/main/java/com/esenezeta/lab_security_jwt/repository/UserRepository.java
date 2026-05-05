package com.esenezeta.lab_security_jwt.repository;

import com.esenezeta.lab_security_jwt.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Método clave para que el JwtAuthenticationFilter busque al usuario real[cite: 1]
    Optional<User> findByUsername(String username);
}