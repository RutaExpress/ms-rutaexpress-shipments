package com.rutaexpress.shipments.application.service;

import com.rutaexpress.shipments.application.port.in.ManageShipmentUseCase;
import com.rutaexpress.shipments.application.port.out.ShipmentNotificationPort;
import com.rutaexpress.shipments.application.port.out.ShipmentRepositoryPort;
import com.rutaexpress.shipments.domain.exception.ShipmentNotFoundException;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.domain.model.ShipmentSearchCriteria;
import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

// Casos de uso de envíos: guarda en Oracle y decide qué comandos publicar
@Slf4j
@Service
public class ShipmentService implements ManageShipmentUseCase {

    private final ShipmentRepositoryPort repositoryPort;
    private final ShipmentNotificationPort notificationPort;

    public ShipmentService(ShipmentRepositoryPort repositoryPort, ShipmentNotificationPort notificationPort) {
        this.repositoryPort = repositoryPort;
        this.notificationPort = notificationPort;
    }

    @Override
    public Shipment createShipment(Shipment shipment) {
        Shipment saved = repositoryPort.save(shipment);
        log.info("[ENVIO-CREADO] id={} tracking={}", saved.getId(), saved.getTrackingNumber());
        return saved;   // CREADO no notifica (el caso notifica desde ACEPTADO)
    }

    @Override
    public Shipment changeStatus(Long id, ShipmentStatus newStatus) {
        Shipment shipment = getShipment(id);
        ShipmentStatus previous = shipment.changeStatus(newStatus);   // regla en el dominio (409 si no se puede)
        Shipment saved = repositoryPort.save(shipment);
        log.info("[ESTADO] envío={} {} -> {}", saved.getId(), previous, newStatus);
        publishCommands(saved, previous);
        return saved;
    }

    @Override
    public Shipment getShipment(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new ShipmentNotFoundException(id));
    }

    @Override
    public List<Shipment> listShipments(ShipmentSearchCriteria criteria) {
        return repositoryPort.search(criteria);
    }

    // Qué comando genera cada estado (tabla de transiciones del contrato)
    private void publishCommands(Shipment shipment, ShipmentStatus previous) {
        switch (shipment.getStatus()) {
            case ACEPTADO -> {
                notificationPort.notifyRecipient(shipment);
                notificationPort.createPickingTicket(shipment);
            }
            case EN_BODEGA -> notificationPort.requestLabel(shipment);
            case EN_RUTA, ENTREGADO -> notificationPort.notifyRecipient(shipment);
            case CANCELADO -> {
                notificationPort.notifyRecipient(shipment);
                if (previous == ShipmentStatus.ACEPTADO || previous == ShipmentStatus.EN_BODEGA) {
                    notificationPort.cancelPickingTicket(shipment);   // solo si ya tenía ticket
                }
            }
            case CREADO -> { }
        }
    }
}
