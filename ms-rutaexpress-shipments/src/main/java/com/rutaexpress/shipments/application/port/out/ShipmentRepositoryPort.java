package com.rutaexpress.shipments.application.port.out;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentSearchCriteria;

import java.util.List;
import java.util.Optional;

// Puerto de salida: persistencia (lo implementa el adaptador JPA/Oracle)
public interface ShipmentRepositoryPort {
    Shipment save(Shipment shipment);
    Optional<Shipment> findById(Long id);
    List<Shipment> search(ShipmentSearchCriteria criteria);
}
