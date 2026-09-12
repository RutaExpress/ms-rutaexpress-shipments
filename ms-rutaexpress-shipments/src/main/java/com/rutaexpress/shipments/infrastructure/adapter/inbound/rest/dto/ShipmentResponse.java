package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto;

import com.rutaexpress.shipments.domain.model.ShipmentStatus;

public record ShipmentResponse(Long id, String trackingNumber, ShipmentStatus status) {}