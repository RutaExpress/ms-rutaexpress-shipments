package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.port.outbound.ShipmentRepositoryPort;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentEntity;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository.SpringDataShipmentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ShipmentPersistenceAdapter implements ShipmentRepositoryPort {

    private final SpringDataShipmentRepository repository;

    public ShipmentPersistenceAdapter(SpringDataShipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Shipment save(Shipment shipment) {
        ShipmentEntity entity = toEntity(shipment);
        ShipmentEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    private ShipmentEntity toEntity(Shipment domain) {
        return ShipmentEntity.builder()
                .id(domain.getId())
                .trackingNumber(domain.getTrackingNumber())
                .originAddress(domain.getOriginAddress())
                .destinationAddress(domain.getDestinationAddress())
                .customerId(domain.getCustomerId())
                .status(domain.getStatus())
                .createdAt(domain.getCreatedAt())
                .updatedAt(domain.getUpdatedAt())
                .build();
    }

    private Shipment toDomain(ShipmentEntity entity) {
        Shipment domain = new Shipment(
                entity.getId(),
                entity.getTrackingNumber(),
                entity.getOriginAddress(),
                entity.getDestinationAddress(),
                entity.getCustomerId(),
                entity.getStatus()
        );
        domain.setCreatedAt(entity.getCreatedAt());
        domain.setUpdatedAt(entity.getUpdatedAt());
        return domain;
    }
}