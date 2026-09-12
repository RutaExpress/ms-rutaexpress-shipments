package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.controller;

import com.rutaexpress.shipments.domain.exception.InvalidStatusTransitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Manejo de Regla de Negocio (Transición de Estado Inválida -> 400)
    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ProblemDetail handleInvalidStatusTransition(InvalidStatusTransitionException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            ex.getMessage()
        );
        problemDetail.setTitle("Transición de estado no permitida");
        problemDetail.setType(URI.create("urn:rutaexpress:error:invalid-status-transition"));
        return problemDetail;
    }

    // 2. Manejo de Recurso No Encontrado (404)
    @ExceptionHandler(RuntimeException.class) // O reemplaza RuntimeException por tu excepción personalizada como ShipmentNotFoundException
    public ProblemDetail handleNotFound(RuntimeException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND, 
            ex.getMessage()
        );
        problemDetail.setTitle("Recurso no encontrado");
        problemDetail.setType(URI.create("urn:rutaexpress:error:not-found"));
        return problemDetail;
    }

    // 3. Manejo de Validaciones de Campos DTO (@Valid -> 400)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
            HttpStatus.BAD_REQUEST, 
            "La solicitud contiene datos de entrada no válidos."
        );
        problemDetail.setTitle("Error de Validación");
        problemDetail.setProperty("invalidFields", errors);
        return problemDetail;
    }
}