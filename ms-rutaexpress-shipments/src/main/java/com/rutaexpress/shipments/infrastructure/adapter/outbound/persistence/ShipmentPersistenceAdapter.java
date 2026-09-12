package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence;

import com.rutaexpress.shipments.application.port.out.ShipmentRepositoryPort;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentJpaEntity;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository.SpringDataShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ShipmentPersistenceAdapter implements ShipmentRepositoryPort {

    private final SpringDataShipmentRepository repository;

    @Override
    public Shipment save(Shipment shipment) {
        ShipmentJpaEntity entity = new ShipmentJpaEntity();
        entity.setId(shipment.getId());
        entity.setTrackingNumber(shipment.getTrackingNumber());
        entity.setStatus(shipment.getStatus());

        ShipmentJpaEntity savedEntity = repository.save(entity);

        Shipment savedShipment = new Shipment();
        savedShipment.setId(savedEntity.getId());
        savedShipment.setTrackingNumber(savedEntity.getTrackingNumber());
        savedShipment.setStatus(savedEntity.getStatus());
        return savedShipment;
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        return repository.findById(id).map(entity -> {
            Shipment shipment = new Shipment();
            shipment.setId(entity.getId());
            shipment.setTrackingNumber(entity.getTrackingNumber());
            shipment.setStatus(entity.getStatus());
            return shipment;
        });
    }
}