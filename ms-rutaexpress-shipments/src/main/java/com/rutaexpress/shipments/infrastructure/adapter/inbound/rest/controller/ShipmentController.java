package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.controller;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.port.inbound.ShipmentUseCase;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.CreateShipmentRequest;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.UpdateStatusRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipments")
public class ShipmentController {

    private final ShipmentUseCase shipmentUseCase;

    public ShipmentController(ShipmentUseCase shipmentUseCase) {
        this.shipmentUseCase = shipmentUseCase;
    }

    @PostMapping
    public ResponseEntity<Shipment> createShipment(@Valid @RequestBody CreateShipmentRequest request) {
        Shipment shipment = new Shipment(
                null,
                request.getTrackingNumber(),
                request.getOriginAddress(),
                request.getDestinationAddress(),
                request.getCustomerId(),
                null
        );
        Shipment created = shipmentUseCase.createShipment(shipment);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Shipment> getShipmentById(@PathVariable Long id) {
        Shipment shipment = shipmentUseCase.getShipmentById(id);
        return ResponseEntity.ok(shipment);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Shipment> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        Shipment updated = shipmentUseCase.updateShipmentStatus(id, request.getStatus());
        return ResponseEntity.ok(updated);
    }
}