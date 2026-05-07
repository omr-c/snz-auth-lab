package com.esenezeta.lab_security_jwt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
        Map<String, String> response = new HashMap<>();

        // Identificamos errores de logica comunes
        if (ex.getMessage().contains("existe") || ex.getMessage().contains("invalidas")) {
            response.put("error", "Validacion SNZ");
            response.put("message", ex.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        response.put("error", "Error Interno del Servidor");
        response.put("message", "Ocurrio un fallo inesperado en la infraestructura.");
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Captura errores cuando el body del JSON es invalido
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleMissingBody(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Body Invalido");
        response.put("message", "El cuerpo de la peticion no puede estar vacio");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}