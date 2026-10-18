package com.rutaexpress.shipments.domain.exception;

// El envío no existe -> 404
public class ShipmentNotFoundException extends RuntimeException {
    public ShipmentNotFoundException(Long id) {
        super("Envío con ID " + id + " no fue encontrado.");
    }
}
