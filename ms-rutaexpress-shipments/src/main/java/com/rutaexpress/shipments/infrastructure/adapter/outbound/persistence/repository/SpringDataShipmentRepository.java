package com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.repository;

import com.rutaexpress.shipments.domain.model.ShipmentStatus;
import com.rutaexpress.shipments.infrastructure.adapter.outbound.persistence.entity.ShipmentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SpringDataShipmentRepository extends JpaRepository<ShipmentJpaEntity, Long> {

    // Filtros opcionales: si el parámetro es null, no filtra por ese campo
    @Query("""
            select s from ShipmentJpaEntity s
            where (:status is null or s.status = :status)
              and (:from is null or s.createdAt >= :from)
              and (:to is null or s.createdAt < :to)
            order by s.createdAt desc
            """)
    List<ShipmentJpaEntity> search(@Param("status") ShipmentStatus status,
                                   @Param("from") LocalDateTime from,
                                   @Param("to") LocalDateTime to);
}
