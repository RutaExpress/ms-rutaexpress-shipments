package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence;

import com.rutaexpress.shipments.application.port.out.ShipmentRepositoryPort;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentSearchCriteria;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentJpaEntity;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository.SpringDataShipmentRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// Adaptador de salida: traduce dominio <-> entidad JPA (guarda TODOS los campos)
@Component
public class ShipmentPersistenceAdapter implements ShipmentRepositoryPort {

    private final SpringDataShipmentRepository repository;

    public ShipmentPersistenceAdapter(SpringDataShipmentRepository repository) {
        this.repository = repository;
    }

    @Override
    public Shipment save(Shipment shipment) {
        return toDomain(repository.save(toEntity(shipment)));
    }

    @Override
    public Optional<Shipment> findById(Long id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Shipment> search(ShipmentSearchCriteria c) {
        LocalDateTime from = c.from() != null ? c.from().atStartOfDay() : null;
        LocalDateTime to = c.to() != null ? c.to().plusDays(1).atStartOfDay() : null;   // "hasta" incluye el día completo
        return repository.search(c.status(), from, to).stream().map(this::toDomain).toList();
    }

    private ShipmentJpaEntity toEntity(Shipment s) {
        ShipmentJpaEntity e = new ShipmentJpaEntity();
        e.setId(s.getId());
        e.setTrackingNumber(s.getTrackingNumber());
        e.setServiceId(s.getServiceId());
        e.setOriginAddress(s.getOriginAddress());
        e.setDestinationAddress(s.getDestinationAddress());
        e.setRecipientName(s.getRecipientName());
        e.setRecipientEmail(s.getRecipientEmail());
        e.setCustomerId(s.getCustomerId());
        e.setStatus(s.getStatus());
        e.setCreatedAt(s.getCreatedAt());
        e.setUpdatedAt(s.getUpdatedAt());
        return e;
    }

    private Shipment toDomain(ShipmentJpaEntity e) {
        return Shipment.restore(e.getId(), e.getTrackingNumber(), e.getServiceId(), e.getOriginAddress(),
                e.getDestinationAddress(), e.getRecipientName(), e.getRecipientEmail(), e.getCustomerId(),
                e.getStatus(), e.getCreatedAt(), e.getUpdatedAt());
    }
}
