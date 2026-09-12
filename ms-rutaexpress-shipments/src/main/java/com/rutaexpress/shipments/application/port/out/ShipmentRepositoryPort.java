package com.rutaexpress.shipments.application.port.out;

import com.rutaexpress.shipments.domain.model.Shipment;
import java.util.Optional;

public interface ShipmentRepositoryPort {
    Shipment save(Shipment shipment);
    Optional<Shipment> findById(Long id);
}