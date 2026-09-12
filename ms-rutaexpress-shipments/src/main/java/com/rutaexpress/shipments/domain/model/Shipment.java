package com.rutaexpress.shipments.domain.model;

import com.rutaexpress.shipments.domain.exception.InvalidStatusTransitionException;
import java.time.LocalDateTime;

public class Shipment {
    private Long id;
    private String trackingNumber;
    private String originAddress;
    private String destinationAddress;
    private String customerId;
    private ShipmentStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Shipment() {}

    public Shipment(Long id, String trackingNumber, String originAddress, String destinationAddress, String customerId, ShipmentStatus status) {
        this.id = id;
        this.trackingNumber = trackingNumber;
        this.originAddress = originAddress;
        this.destinationAddress = destinationAddress;
        this.customerId = customerId;
        this.status = status != null ? status : ShipmentStatus.CREADO;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Regla de Negocio  No se puede pasar a EN_RUTA sin antes ser ACEPTADO
    public void changeStatus(ShipmentStatus newStatus) {
        if (newStatus == ShipmentStatus.EN_RUTA && this.status == ShipmentStatus.CREADO) {
            throw new InvalidStatusTransitionException(
                "Regla de Negocio: No se puede cambiar a EN_RUTA un envío en estado CREADO sin haber sido ACEPTADO previamente."
            );
        }
        this.status = newStatus;
        this.updatedAt = LocalDateTime.now();
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getOriginAddress() { return originAddress; }
    public void setOriginAddress(String originAddress) { this.originAddress = originAddress; }
    public String getDestinationAddress() { return destinationAddress; }
    public void setDestinationAddress(String destinationAddress) { this.destinationAddress = destinationAddress; }
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    public ShipmentStatus getStatus() { return status; }
    public void setStatus(ShipmentStatus status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}