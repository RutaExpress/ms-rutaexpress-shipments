package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto;

import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    @NotNull(message = "El nuevo estado no puede ser nulo")
    private ShipmentStatus status;
}