package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto;

import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import jakarta.validation.constraints.NotNull;

// Body de PUT /api/v1/shipments/{id}/status
public record ShipmentStatusUpdateRequest(
        @NotNull(message = "status es obligatorio")
        ShipmentStatus status
) {}
