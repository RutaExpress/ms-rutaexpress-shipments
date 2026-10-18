package com.rutaexpress.shipments.domain.model;

import java.util.EnumSet;
import java.util.Set;

// Estados del envío y transiciones permitidas (regla del caso)
public enum ShipmentStatus {
    CREADO,
    ACEPTADO,
    EN_BODEGA,
    EN_RUTA,
    ENTREGADO,
    CANCELADO;

    // A qué estados puede pasar desde el actual
    public Set<ShipmentStatus> allowedNext() {
        return switch (this) {
            case CREADO -> EnumSet.of(ACEPTADO, CANCELADO);              // no puede ir a EN_RUTA sin ACEPTAR
            case ACEPTADO -> EnumSet.of(EN_BODEGA, EN_RUTA, CANCELADO);
            case EN_BODEGA -> EnumSet.of(EN_RUTA, CANCELADO);
            case EN_RUTA -> EnumSet.of(ENTREGADO);
            case ENTREGADO, CANCELADO -> EnumSet.noneOf(ShipmentStatus.class); // estados finales
        };
    }

    public boolean canTransitionTo(ShipmentStatus next) {
        return allowedNext().contains(next);
    }
}
