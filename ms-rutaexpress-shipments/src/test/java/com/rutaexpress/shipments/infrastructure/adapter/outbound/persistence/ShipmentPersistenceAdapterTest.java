package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentJpaEntity;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository.SpringDataShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentPersistenceAdapterTest {

    @Mock
    private SpringDataShipmentRepository repository;

    @InjectMocks
    private ShipmentPersistenceAdapter adapter;

    @Test
    void shouldSaveShipmentSuccessfully() {
        Shipment domainShipment = new Shipment();
        domainShipment.setTrackingNumber("TRK-123");
        domainShipment.setStatus(ShipmentStatus.CREADO);

        ShipmentJpaEntity savedEntity = new ShipmentJpaEntity();
        savedEntity.setId(1L);
        savedEntity.setTrackingNumber("TRK-123");
        savedEntity.setStatus(ShipmentStatus.CREADO);

        when(repository.save(any(ShipmentJpaEntity.class))).thenReturn(savedEntity);

        Shipment result = adapter.save(domainShipment);

        assertNotNull(result.getId());
        assertEquals("TRK-123", result.getTrackingNumber());
        verify(repository, times(1)).save(any(ShipmentJpaEntity.class));
    }

    @Test
    void shouldFindShipmentById() {
        ShipmentJpaEntity entity = new ShipmentJpaEntity();
        entity.setId(1L);
        entity.setStatus(ShipmentStatus.ACEPTADO);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<Shipment> result = adapter.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(ShipmentStatus.ACEPTADO, result.get().getStatus());
    }
}