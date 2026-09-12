package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository;

import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataShipmentRepository extends JpaRepository<ShipmentJpaEntity, Long> {
}