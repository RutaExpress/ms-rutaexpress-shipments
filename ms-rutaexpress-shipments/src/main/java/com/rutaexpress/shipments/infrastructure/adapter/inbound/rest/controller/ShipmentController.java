package com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.controller;

import com.rutaexpress.shipments.application.port.in.ManageShipmentUseCase;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentSearchCriteria;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.CreateShipmentRequest;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.ShipmentResponse;
import com.rutaexpress.shipments.infrastructure.adapter.inbound.rest.dto.ShipmentStatusUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

// Endpoints de envíos (los llama el BFF)
@RestController
@RequestMapping("/api/v1/shipments")
public class ShipmentController {

    private final ManageShipmentUseCase useCase;

    public ShipmentController(ManageShipmentUseCase useCase) {
        this.useCase = useCase;
    }

    // POST /api/v1/shipments  (X-User-Id lo agrega el BFF con el id del JWT)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShipmentResponse create(@Valid @RequestBody CreateShipmentRequest req,
                                   @RequestHeader(value = "X-User-Id", required = false) String userId) {
        Shipment shipment = Shipment.create(req.serviceId(), req.originAddress(), req.destinationAddress(),
                req.recipientName(), req.recipientEmail(), userId);
        return ShipmentResponse.from(useCase.createShipment(shipment));
    }

    // GET /api/v1/shipments?status=ACEPTADO&from=2026-10-01&to=2026-10-31 (todos opcionales)
    @GetMapping
    public List<ShipmentResponse> list(
            @RequestParam(required = false) ShipmentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return useCase.listShipments(new ShipmentSearchCriteria(status, from, to)).stream()
                .map(ShipmentResponse::from)
                .toList();
    }

    // GET /api/v1/shipments/{id}
    @GetMapping("/{id}")
    public ShipmentResponse get(@PathVariable Long id) {
        return ShipmentResponse.from(useCase.getShipment(id));
    }

    // PUT /api/v1/shipments/{id}/status  body: { "status": "ACEPTADO" }  (como dice el caso)
    @PutMapping("/{id}/status")
    public ShipmentResponse changeStatus(@PathVariable Long id, @Valid @RequestBody ShipmentStatusUpdateRequest req) {
        return ShipmentResponse.from(useCase.changeStatus(id, req.status()));
    }
}
