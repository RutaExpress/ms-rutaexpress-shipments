package com.rutaexpress.shipments.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ShipmentTest {

    @Test
    void shouldCreateShipmentCorrectly() {
        Shipment shipment = new Shipment(1L, "TRK-100", "Origen", "Destino", "CLI-01", null);

        assertNotNull(shipment);
        assertEquals("TRK-100", shipment.getTrackingNumber());
    }
}