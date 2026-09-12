package com.rutaexpress.shipments.application.service;

import com.rutaexpress.shipments.application.port.out.ShipmentRepositoryPort;
import com.rutaexpress.shipments.domain.exception.ShipmentNotFoundException;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
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
class ShipmentServiceTest {

    @Mock
    private ShipmentRepositoryPort repositoryPort;

    @InjectMocks
    private ShipmentService shipmentService;

    @Test
    void shouldCreateShipmentWithStatusCreado() {
        Shipment input = new Shipment();
        Shipment saved = new Shipment();
        saved.setStatus(ShipmentStatus.CREADO);

        when(repositoryPort.save(any(Shipment.class))).thenReturn(saved);

        Shipment result = shipmentService.createShipment(input);

        assertEquals(ShipmentStatus.CREADO, result.getStatus());
        verify(repositoryPort, times(1)).save(input);
    }

    @Test
    void shouldChangeShipmentStatusSuccessfully() {
        Shipment existing = new Shipment();
        existing.setStatus(ShipmentStatus.CREADO);
        
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(existing));
        when(repositoryPort.save(any(Shipment.class))).thenReturn(existing);

        Shipment result = shipmentService.changeStatus(1L, ShipmentStatus.ACEPTADO);

        assertEquals(ShipmentStatus.ACEPTADO, result.getStatus());
        verify(repositoryPort).save(existing);
    }

    @Test
    void shouldThrowExceptionWhenChangingStatusOfNonExistentShipment() {
        when(repositoryPort.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ShipmentNotFoundException.class, 
            () -> shipmentService.changeStatus(99L, ShipmentStatus.ACEPTADO));
        
        verify(repositoryPort, never()).save(any());
    }
}