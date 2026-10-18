package com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging.dto;

// Mensaje para q.cmd.warehouse (mismos campos que en notify)
public record WarehouseTicketCommand(
        String eventId,
        String shipmentId,
        String originAddress,
        String action   // CREATE o CANCEL
) {}
