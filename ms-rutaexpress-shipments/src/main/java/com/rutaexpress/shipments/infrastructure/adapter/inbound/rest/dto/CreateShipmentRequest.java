package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateShipmentRequest {
    @NotBlank(message = "El tracking number es obligatorio")
    private String trackingNumber;

    @NotBlank(message = "La dirección de origen es obligatoria")
    private String originAddress;

    @NotBlank(message = "La dirección de destino es obligatoria")
    private String destinationAddress;

    @NotBlank(message = "El cliente es obligatorio")
    private String customerId;
}