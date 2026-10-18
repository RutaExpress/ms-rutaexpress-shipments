package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence;

import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentSearchCriteria;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentJpaEntity;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository.SpringDataShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

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
    void savePersistsAllFields() {
        Shipment s = Shipment.create("SRV-1", "Origen", "Destino", "Juan", "juan@correo.cl", "user-1");
        when(repository.save(any(ShipmentJpaEntity.class))).thenAnswer(inv -> {
            ShipmentJpaEntity e = inv.getArgument(0);
            e.setId(10L);
            return e;
        });

        Shipment saved = adapter.save(s);

        assertEquals(10L, saved.getId());
        assertEquals("juan@correo.cl", saved.getRecipientEmail());
        assertEquals("Destino", saved.getDestinationAddress());
        assertEquals(ShipmentStatus.CREADO, saved.getStatus());
    }

    @Test
    void searchIncludesWholeToDay() {
        when(repository.search(any(), any(), any())).thenReturn(List.of());

        adapter.search(new ShipmentSearchCriteria(ShipmentStatus.ACEPTADO,
                LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 31)));

        verify(repository).search(ShipmentStatus.ACEPTADO,
                LocalDate.of(2026, 10, 1).atStartOfDay(), LocalDate.of(2026, 11, 1).atStartOfDay());
    }
}
