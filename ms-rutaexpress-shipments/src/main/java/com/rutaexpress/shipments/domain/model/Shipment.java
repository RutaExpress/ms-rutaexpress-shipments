package com.rutaexpress.shipments.domain.model;

import com.rutaexpress.shipments.domain.exception.InvalidStatusTransitionException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

// Agregado Envío: guarda sus datos y protege la regla de cambio de estado
public class Shipment {

    private final Long id;
    private final String trackingNumber;
    private final String serviceId;
    private final String originAddress;
    private final String destinationAddress;
    private final String recipientName;
    private final String recipientEmail;   // a quien se notifica (destinatario)
    private final String customerId;       // quien lo creó (viene del JWT vía BFF)
    private ShipmentStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Shipment(Long id, String trackingNumber, String serviceId, String originAddress,
                     String destinationAddress, String recipientName, String recipientEmail,
                     String customerId, ShipmentStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.serviceId = serviceId;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.recipientName = recipientName;
        this.recipientEmail = recipientEmail;
        this.customerId = customerId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Envío nuevo: estado CREADO y tracking generado por el backend
    public static Shipment create(String serviceId, String originAddress, String destinationAddress,
                                  String recipientName, String recipientEmail, String customerId) {
        LocalDateTime now = LocalDateTime.now();
        return new Shipment(null, newTrackingNumber(), serviceId, originAddress, destinationAddress,
                recipientName, recipientEmail, customerId, ShipmentStatus.CREADO, now, now);
    }

    // Reconstruye un envío leído de la base de datos
    public static Shipment restore(Long id, String trackingNumber, String serviceId, String originAddress,
                                   String destinationAddress, String recipientName, String recipientEmail,
                                   String customerId, ShipmentStatus status, LocalDateTime createdAt,
                                   LocalDateTime updatedAt) {
        return new Shipment(id, trackingNumber, serviceId, originAddress, destinationAddress,
                recipientName, recipientEmail, customerId, status, createdAt, updatedAt);
    }

    // Cambia el estado si la transición está permitida. Devuelve el estado anterior
    public ShipmentStatus changeStatus(ShipmentStatus next) {
        if (!status.canTransitionTo(next)) {
            throw new InvalidStatusTransitionException(
                    "No se puede pasar de " + status + " a " + next + ". Permitidos: " + status.allowedNext());
        }
        ShipmentStatus previous = status;
        status = next;
        updatedAt = LocalDateTime.now();
        return previous;
    }

    // Ej: RX-20261018-A1B2C3
    private static String newTrackingNumber() {
        return "RX-" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE) + "-"
                + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
    }

    public Long getId() { return id; }
    public String getTrackingNumber() { return trackingNumber; }
    public String getServiceId() { return serviceId; }
    public String getOriginAddress() { return originAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public String getRecipientName() { return recipientName; }
    public String getRecipientEmail() { return recipientEmail; }
    public String getCustomerId() { return customerId; }
    public ShipmentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
