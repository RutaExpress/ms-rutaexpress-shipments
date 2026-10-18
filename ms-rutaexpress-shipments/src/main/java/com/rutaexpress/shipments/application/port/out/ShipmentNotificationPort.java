package com.rutaexpress.shipments.application.port.out;

import com.rutaexpress.shipments.domain.model.Shipment;

// Puerto de salida: comandos asíncronos (lo implementa el productor RabbitMQ)
public interface ShipmentNotificationPort {
    void notifyRecipient(Shipment shipment);       // email al destinatario
    void createPickingTicket(Shipment shipment);   // ticket de bodega
    void cancelPickingTicket(Shipment shipment);   // anular ticket
    void requestLabel(Shipment shipment);          // etiqueta PDF
}
