package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// Body de POST /api/v1/shipments (el tracking y el estado los pone el backend)
public record CreateShipmentRequest(
        @NotBlank(message = "serviceId es obligatorio")
        String serviceId,

        @NotBlank(message = "originAddress es obligatoria")
        @Size(max = 255, message = "originAddress: máximo 255 caracteres")
        String originAddress,

        @NotBlank(message = "destinationAddress es obligatoria")
        @Size(max = 255, message = "destinationAddress: máximo 255 caracteres")
        String destinationAddress,

        @NotBlank(message = "recipientName es obligatorio")
        @Size(max = 120, message = "recipientName: máximo 120 caracteres")
        String recipientName,

        @NotBlank(message = "recipientEmail es obligatorio")
        @Email(message = "recipientEmail no tiene formato de email")
        String recipientEmail
) {}
