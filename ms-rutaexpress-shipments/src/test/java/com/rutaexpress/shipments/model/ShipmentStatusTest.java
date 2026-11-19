package com.rutaexpress.shipments.model;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ShipmentStatusTest {
    //METODO 1 CAMBIOS DE ESTADO PERMITIDO
    //anotacion junit test con parametro; 0 y 1 primer y segundo valor de cada fila 
    @ParameterizedTest(name = "primer valor {0} -> {1} segundo valor de la fila")

    //datos fila por fila  (0,1)
    @CsvSource({
        "CREADO, ACEPTADO",
        "CREADO, CANCELADO",
        "ACEPTADO, EN_BODEGA",
        "ACEPTADO, CANCELADO",
        "EN_BODEGA, EN_RUTA",
        "EN_BODEGA, CANCELADO",
        "EN_RUTA, ENTREGADO"
    })
    
    //metodo void pq no hay retorno nombre metodo allo.(cambios de estado permitidas)
    //ShipmentStatus nombre de la clase de estados enum

    void allowedTransitions( ShipmentStatus from , ShipmentStatus to) {
        // Arrange: from y to llegan desde @CsvSource

        //act
        boolean puede = from.canTransitionTo(to);

        //Assert
        assertTrue(puede);
    }

    //METODO 2 CAMBIOS DE ESTADO NO PERMITIDOS
    ///anotacion junit test con parametro; 0 y 1 primer y segundo valor de cada fila 
    @ParameterizedTest(name = "primer valor {0} -> {1} segundo valor de la fila")

    //datos fila por fila  (0,1)
    @CsvSource({
        "CREADO, EN_RUTA",
        "ACEPTADO, EN_RUTA"
    })

    // METODO FORB..(transicion prohibida) 
    void forbiddenTransitions(ShipmentStatus from , ShipmentStatus to){
        // Arrange: from y to llegan desde @CsvSource
        
        //act
        boolean puede = from.canTransitionTo(to);

        //Assert
        assertFalse(puede);
    }

    //METODO 3: LOS ESTADOS FINALES NO TIENEN SALIDA (ningún cambio permitido)
    ///anotacion junit test con parametro; 0 PRIMER VALOR DEL ESTADO
    @ParameterizedTest(name = "{0} ES EL ESTADO FINAL  ")

    //datos: constantes del enum ShipmentStatus, solo las que van en names
    @EnumSource(value = ShipmentStatus.class, names = {"ENTREGADO","CANCELADO"})

    //metodo void pq no hay retorno; nombre finalSt..(los estados finales no tienen salida)
    void finalStatesHaveNoExit(ShipmentStatus estado ){
        // Arrange: estado llega desde @EnumSource
        
        //act
        boolean estadoFinal = estado.allowedNext().isEmpty();

        //Assert
        assertTrue(estadoFinal);
    }



}