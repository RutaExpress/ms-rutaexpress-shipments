package com.rutaexpress.shipments.application.service;

import com.rutaexpress.shipments.domain.exception.ShipmentNotFoundException;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import com.rutaexpress.shipments.domain.port.inbound.ShipmentUseCase;
import com.rutaexpress.shipments.domain.port.outbound.ShipmentRepositoryPort;
import org.springframework.stereotype.Service;

@Service
public class ShipmentService implements ShipmentUseCase {

    private final ShipmentRepositoryPort shipmentRepositoryPort;

    public ShipmentService(ShipmentRepositoryPort shipmentRepositoryPort) {
        this.shipmentRepositoryPort = shipmentRepositoryPort;
    }

    @Override
    public Shipment createShipment(Shipment shipment) {
        shipment.setStatus(ShipmentStatus.CREADO);
        return shipmentRepositoryPort.save(shipment);
    }

    @Override
    public Shipment getShipmentById(Long id) {
        return shipmentRepositoryPort.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));
    }

    @Override
    public Shipment updateShipmentStatus(Long id, ShipmentStatus newStatus) {
        Shipment shipment = getShipmentById(id);
        shipment.changeStatus(newStatus); // Ejecuta la validación de dominio
        return shipmentRepositoryPort.save(shipment);
    }
}