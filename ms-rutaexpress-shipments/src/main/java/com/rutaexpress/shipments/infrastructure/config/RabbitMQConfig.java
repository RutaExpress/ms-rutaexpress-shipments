package com.rutaexpress.shipments.infrastructure.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Configuración del PRODUCTOR: nombres centralizados y exchanges donde publica.
// Las colas, DLQ y bindings los declara el consumidor (ms-rutaexpress-notify).
@Configuration
public class RabbitMQConfig {

    // Exchanges (mismos nombres y tipo que en notify)
    public static final String DIRECT_EXCHANGE = "cmd.direct";
    public static final String TOPIC_EXCHANGE = "cmd.topic";

    // Routing keys que usa shipments
    public static final String RK_EMAIL_PREFIX = "email.";                    // email.aceptado, email.en_ruta... (topic email.*)
    public static final String RK_WAREHOUSE_CREATE = "warehouse.ticket.create"; // topic warehouse.#
    public static final String RK_WAREHOUSE_CANCEL = "warehouse.ticket.cancel"; // topic warehouse.#
    public static final String RK_LABEL_GEN = "label.gen";                    // direct (key exacta)

    // Tipos de mensaje (propiedad "type" del mensaje AMQP)
    public static final String TYPE_EMAIL = "shipment.email";
    public static final String TYPE_TICKET = "shipment.warehouse-ticket";
    public static final String TYPE_LABEL = "shipment.label";

    // Declarar igual que el consumidor es idempotente: si ya existen, no cambia nada
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(DIRECT_EXCHANGE, true, false);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(TOPIC_EXCHANGE, true, false);
    }
}
