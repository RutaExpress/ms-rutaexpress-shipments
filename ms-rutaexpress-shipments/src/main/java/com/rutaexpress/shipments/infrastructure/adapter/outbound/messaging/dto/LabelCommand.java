package com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging.dto;

// Mensaje para q.cmd.label (mismos campos que en notify)
public record LabelCommand(
        String eventId,
        String shipmentId,
        String recipientName,
        String destinationAddress
) {}
