package com.rutaexpress.shipments.application.port.in;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentSearchCriteria;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;

import java.util.List;

// Puerto de entrada: lo que el microservicio ofrece (lo usa el controller)
public interface ManageShipmentUseCase {
    Shipment createShipment(Shipment shipment);
    Shipment changeStatus(Long id, ShipmentStatus newStatus);
    Shipment getShipment(Long id);
    List<Shipment> listShipments(ShipmentSearchCriteria criteria);
}
