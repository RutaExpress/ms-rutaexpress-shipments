package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;

import java.time.LocalDateTime;

// Respuesta de la API (contrato con el front)
public record ShipmentResponse(
        Long id,
        String trackingNumber,
        String serviceId,
        String originAddress,
        String destinationAddress,
        String recipientName,
        String recipientEmail,
        String customerId,
        ShipmentStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ShipmentResponse from(Shipment s) {
        return new ShipmentResponse(s.getId(), s.getTrackingNumber(), s.getServiceId(), s.getOriginAddress(),
                s.getDestinationAddress(), s.getRecipientName(), s.getRecipientEmail(), s.getCustomerId(),
                s.getStatus(), s.getCreatedAt(), s.getUpdatedAt());
    }
}
