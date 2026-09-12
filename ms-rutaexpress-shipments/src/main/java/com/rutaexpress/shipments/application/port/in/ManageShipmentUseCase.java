package com.rutaexpress.shipments.application.port.in;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;

public interface ManageShipmentUseCase {
    Shipment createShipment(Shipment shipment);
    Shipment changeStatus(Long id, ShipmentStatus newStatus);
    Shipment getShipment(Long id);
}