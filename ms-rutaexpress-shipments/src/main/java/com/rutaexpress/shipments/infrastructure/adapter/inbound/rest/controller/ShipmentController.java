package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.controller;

import com.rutaexpress.shipments.application.port.in.ManageShipmentUseCase;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.ShipmentCreateRequest;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.ShipmentResponse;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.ShipmentStatusUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentController {

    private final ManageShipmentUseCase manageShipmentUseCase;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse createShipment(@RequestBody ShipmentCreateRequest request) {
        Shipment shipment = new Shipment();
        shipment.setTrackingNumber(request.trackingNumber());
        
        Shipment created = manageShipmentUseCase.createShipment(shipment);
        return new ShipmentResponse(created.getId(), created.getTrackingNumber(), created.getStatus());
    }

    @GetMapping("/{id}")
    public ShipmentResponse getShipment(@PathVariable Long id) {
        Shipment shipment = manageShipmentUseCase.getShipment(id);
        return new ShipmentResponse(shipment.getId(), shipment.getTrackingNumber(), shipment.getStatus());
    }

    @PatchMapping("/{id}/status")
    public ShipmentResponse updateStatus(@PathVariable Long id, @RequestBody ShipmentStatusUpdateRequest request) {
        Shipment updated = manageShipmentUseCase.changeStatus(id, request.status());
        return new ShipmentResponse(updated.getId(), updated.getTrackingNumber(), updated.getStatus());
    }
}