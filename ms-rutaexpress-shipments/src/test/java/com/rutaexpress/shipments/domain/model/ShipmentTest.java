package com.rutaexpress.shipments.domain.model;

import com.rutaexpress.shipments.domain.exception.InvalidStatusTransitionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    private Shipment newShipment() {
        return Shipment.create("SRV-1", "Origen 1", "Destino 2", "Juan Soto", "juan@correo.cl", "user-1");
    }

    @Test
    void createStartsInCreadoWithTracking() {
        Shipment s = newShipment();
        assertEquals(ShipmentStatus.CREADO, s.getStatus());
        assertTrue(s.getTrackingNumber().startsWith("RX-"));
        assertNotNull(s.getCreatedAt());
    }

    @Test
    void cannotGoToEnRutaWithoutAceptado() {
        Shipment s = newShipment();
        assertThrows(InvalidStatusTransitionException.class, () -> s.changeStatus(ShipmentStatus.EN_RUTA));
        assertEquals(ShipmentStatus.CREADO, s.getStatus());
    }

    @Test
    void changeStatusReturnsPreviousStatus() {
        Shipment s = newShipment();
        ShipmentStatus previous = s.changeStatus(ShipmentStatus.ACEPTADO);
        assertEquals(ShipmentStatus.CREADO, previous);
        assertEquals(ShipmentStatus.ACEPTADO, s.getStatus());
    }

    @Test
    void finalStatesCannotChange() {
        Shipment s = newShipment();
        s.changeStatus(ShipmentStatus.CANCELADO);
        assertThrows(InvalidStatusTransitionException.class, () -> s.changeStatus(ShipmentStatus.ACEPTADO));
    }
}
