package com.rutaexpress.shipments.domain.exception;

// Transición de estado no permitida -> 409
public class InvalidStatusTransitionException extends RuntimeException {
    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}
