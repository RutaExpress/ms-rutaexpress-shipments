package com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging.dto;

// Mensaje para q.cmd.email (mismos campos que ShipmentEvent en notify)
public record EmailCommand(
        String eventId,
        String shipmentId,
        String status,
        String customerEmail,   // email del destinatario
        String message
) {}
