package com.rutaexpress.shipments.application.service;

import com.rutaexpress.shipments.application.port.out.ShipmentNotificationPort;
import com.rutaexpress.shipments.application.port.out.ShipmentRepositoryPort;
import com.rutaexpress.shipments.domain.exception.InvalidStatusTransitionException;
import com.rutaexpress.shipments.domain.exception.ShipmentNotFoundException;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepositoryPort repositoryPort;

    @Mock
    private ShipmentNotificationPort notificationPort;

    @InjectMocks
    private ShipmentService service;

    private Shipment existing(ShipmentStatus status) {
        return Shipment.restore(1L, "RX-1", "SRV-1", "Origen", "Destino", "Juan", "juan@correo.cl",
                "user-1", status, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void createSavesWithoutPublishing() {
        Shipment input = Shipment.create("SRV-1", "Origen", "Destino", "Juan", "juan@correo.cl", "user-1");
        when(repositoryPort.save(input)).thenReturn(input);

        assertEquals(ShipmentStatus.CREADO, service.createShipment(input).getStatus());
        verifyNoInteractions(notificationPort);
    }

    @Test
    void aceptadoPublishesEmailAndTicket() {
        Shipment s = existing(ShipmentStatus.CREADO);
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(s));
        when(repositoryPort.save(any(Shipment.class))).thenAnswer(inv -> inv.getArgument(0));

        service.changeStatus(1L, ShipmentStatus.ACEPTADO);

        verify(notificationPort).notifyRecipient(s);
        verify(notificationPort).createPickingTicket(s);
        verifyNoMoreInteractions(notificationPort);
    }

    @Test
    void enBodegaPublishesLabel() {
        Shipment s = existing(ShipmentStatus.ACEPTADO);
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(s));
        when(repositoryPort.save(any(Shipment.class))).thenAnswer(inv -> inv.getArgument(0));

        service.changeStatus(1L, ShipmentStatus.EN_BODEGA);

        verify(notificationPort).requestLabel(s);
        verifyNoMoreInteractions(notificationPort);
    }

    @Test
    void canceladoFromCreadoDoesNotCancelTicket() {
        Shipment s = existing(ShipmentStatus.CREADO);
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(s));
        when(repositoryPort.save(any(Shipment.class))).thenAnswer(inv -> inv.getArgument(0));

        service.changeStatus(1L, ShipmentStatus.CANCELADO);

        verify(notificationPort).notifyRecipient(s);
        verify(notificationPort, never()).cancelPickingTicket(any());
    }

    @Test
    void invalidTransitionDoesNotSaveNorPublish() {
        when(repositoryPort.findById(1L)).thenReturn(Optional.of(existing(ShipmentStatus.CREADO)));

        assertThrows(InvalidStatusTransitionException.class,
                () -> service.changeStatus(1L, ShipmentStatus.EN_RUTA));
        verify(repositoryPort, never()).save(any());
        verifyNoInteractions(notificationPort);
    }

    @Test
    void notFoundThrows() {
        when(repositoryPort.findById(99L)).thenReturn(Optional.empty());
        assertThrows(ShipmentNotFoundException.class, () -> service.changeStatus(99L, ShipmentStatus.ACEPTADO));
    }
}
