package com.rutaexpress.shipments.application.service;

import com.rutaexpress.shipments.application.port.in.ManageShipmentUseCase;
import com.rutaexpress.shipments.application.port.out.ShipmentRepositoryPort;
import com.rutaexpress.shipments.domain.exception.ShipmentNotFoundException;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShipmentService implements ManageShipmentUseCase {

    private final ShipmentRepositoryPort repositoryPort;

    @Override
    public Shipment createShipment(Shipment shipment) {
        shipment.setStatus(ShipmentStatus.CREADO);
        return repositoryPort.save(shipment);
    }

    @Override
    public Shipment changeStatus(Long id, ShipmentStatus newStatus) {
        Shipment shipment = repositoryPort.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));
        
        // Aquí se ejecuta la validación de la regla de negocio
        shipment.changeStatus(newStatus);
        
        return repositoryPort.save(shipment);
    }

    @Override
    public Shipment getShipment(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));
    }
}