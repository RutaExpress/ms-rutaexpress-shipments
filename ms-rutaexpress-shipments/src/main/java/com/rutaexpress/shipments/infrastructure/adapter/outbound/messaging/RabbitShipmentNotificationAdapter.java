package com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging;

import com.rutaexpress.shipments.application.port.out.ShipmentNotificationPort;
import com.rutaexpress.shipments.domain.model.Shipment;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging.dto.EmailCommand;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging.dto.LabelCommand;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.messaging.dto.WarehouseTicketCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

import java.util.Date;
import java.util.UUID;

import static com.rutaexpress.shipments.infrastructure.config.RabbitMQConfig.*;

// PRODUCTOR: convierte cada comando en JSON y lo publica en RabbitMQ
@Slf4j
@Component
public class RabbitShipmentNotificationAdapter implements ShipmentNotificationPort {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public RabbitShipmentNotificationAdapter(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void notifyRecipient(Shipment s) {
        String eventId = newEventId();
        String text = "Su envío " + s.getTrackingNumber() + " ahora está " + s.getStatus();
        EmailCommand cmd = new EmailCommand(eventId, String.valueOf(s.getId()), s.getStatus().name(),
                s.getRecipientEmail(), text);
        // topic: email.aceptado, email.en_ruta... -> patrón email.* -> q.cmd.email
        publish(TOPIC_EXCHANGE, RK_EMAIL_PREFIX + s.getStatus().name().toLowerCase(), TYPE_EMAIL, eventId, cmd);
    }

    @Override
    public void createPickingTicket(Shipment s) {
        String eventId = newEventId();
        WarehouseTicketCommand cmd = new WarehouseTicketCommand(eventId, String.valueOf(s.getId()),
                s.getOriginAddress(), "CREATE");
        publish(TOPIC_EXCHANGE, RK_WAREHOUSE_CREATE, TYPE_TICKET, eventId, cmd);
    }

    @Override
    public void cancelPickingTicket(Shipment s) {
        String eventId = newEventId();
        WarehouseTicketCommand cmd = new WarehouseTicketCommand(eventId, String.valueOf(s.getId()),
                s.getOriginAddress(), "CANCEL");
        publish(TOPIC_EXCHANGE, RK_WAREHOUSE_CANCEL, TYPE_TICKET, eventId, cmd);
    }

    @Override
    public void requestLabel(Shipment s) {
        String eventId = newEventId();
        LabelCommand cmd = new LabelCommand(eventId, String.valueOf(s.getId()),
                s.getRecipientName(), s.getDestinationAddress());
        // direct: key exacta label.gen -> q.cmd.label
        publish(DIRECT_EXCHANGE, RK_LABEL_GEN, TYPE_LABEL, eventId, cmd);
    }

    // Envelope: el payload va en el body y los metadatos en las propiedades AMQP
    private void publish(String exchange, String routingKey, String type, String eventId, Object payload) {
        try {
            Message message = MessageBuilder.withBody(objectMapper.writeValueAsBytes(payload))
                    .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                    .setContentEncoding("UTF-8")
                    .setMessageId(eventId)
                    .setType(type)
                    .setTimestamp(new Date())
                    .build();
            rabbitTemplate.send(exchange, routingKey, message);
            log.info("[PUBLICADO] exchange={} key={} tipo={} eventId={}", exchange, routingKey, type, eventId);
        } catch (AmqpException | JacksonException e) {
            // El envío ya quedó guardado: si RabbitMQ falla no se cae la operación (desacople)
            log.error("[PUBLICACION-FALLIDA] exchange={} key={} eventId={} motivo={}",
                    exchange, routingKey, eventId, e.getMessage());
        }
    }

    private String newEventId() {
        return UUID.randomUUID().toString();
    }
}
