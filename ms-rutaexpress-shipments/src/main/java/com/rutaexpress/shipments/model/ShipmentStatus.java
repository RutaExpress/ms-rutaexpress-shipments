package com.rutaexpress.shipments.model;

import java.util.EnumSet;
import java.util.Set;

public enum ShipmentStatus {

    CREADO,
    ACEPTADO,
    EN_BODEGA,
    EN_RUTA,
    ENTREGADO,
    CANCELADO;


    //metodo allowedNext (siguientes permitidos)
    public Set<ShipmentStatus> allowedNext() {
        return switch (this){
        case CREADO -> EnumSet.of(ACEPTADO, CANCELADO);
        case ACEPTADO -> EnumSet.of(CANCELADO, EN_BODEGA);
        case EN_BODEGA -> EnumSet.of(CANCELADO, EN_RUTA);
        case EN_RUTA -> EnumSet.of(ENTREGADO);
        case ENTREGADO , CANCELADO-> EnumSet.noneOf(ShipmentStatus.class);};
    }

    //metodo canTransitionTo (puede pasar a)
    public boolean canTransitionTo(ShipmentStatus next) {
        return allowedNext().contains(next);
    }
    


        
}
