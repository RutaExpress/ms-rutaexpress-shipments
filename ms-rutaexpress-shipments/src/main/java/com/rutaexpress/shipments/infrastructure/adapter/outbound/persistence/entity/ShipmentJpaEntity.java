package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity;

import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

// Tabla SHIPMENTS en Oracle. Columnas nuevas nullable para no romper filas existentes (ddl-auto=update)
@Entity
@Table(name = "shipments")
@Getter
@Setter
public class ShipmentJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String trackingNumber;

    @Column(length = 64)
    private String serviceId;

    @Column(length = 255)
    private String originAddress;

    @Column(length = 255)
    private String destinationAddress;

    @Column(length = 120)
    private String recipientName;

    @Column(length = 160)
    private String recipientEmail;

    @Column(length = 64)
    private String customerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ShipmentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
