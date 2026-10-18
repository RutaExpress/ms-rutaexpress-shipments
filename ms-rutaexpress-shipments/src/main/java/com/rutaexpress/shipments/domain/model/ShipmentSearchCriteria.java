package com.rutaexpress.shipments.domain.model;

import java.time.LocalDate;

// Filtros del listado. Todos opcionales (null = sin filtro)
public record ShipmentSearchCriteria(
        ShipmentStatus status,
        LocalDate from,   // desde (incluido)
        LocalDate to      // hasta (incluido)
) {}
