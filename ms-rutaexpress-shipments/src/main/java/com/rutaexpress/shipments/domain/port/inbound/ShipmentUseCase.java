package com.rutaexpress.shipments.domain.port.inbound;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;

public interface ShipmentUseCase {
    Shipment createShipment(Shipment shipment);
    Shipment getShipmentById(Long id);
    Shipment updateShipmentStatus(Long id, ShipmentStatus newStatus);
}