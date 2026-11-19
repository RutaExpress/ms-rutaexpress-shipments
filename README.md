# ms-rutaexpress-shipments

Microservicio de **envíos** del caso RutaExpress  crear y consultar envíos, cambiar su estado con las reglas del caso, guardar en **Oracle** y publicar en **RabbitMQ** los comandos que consume `ms-rutaexpress-notify`.

> 🚧 **Rama `refactor/ArquitecturaCapas`: refactor en curso.**
> Esta rama reemplaza la arquitectura hexagonal por **arquitectura en capas** (deuda técnica del proyecto).
> La versión que funciona completa sigue en **`main`**. Esta rama se une a `main` por pull request cuando el refactor esté terminado y probado.

## Tecnologías
Java 21 · Spring Boot 4.1.2 · Spring Data JPA (Oracle) · Spring AMQP (RabbitMQ) · Bean Validation · Lombok · JUnit 5 y Mockito

## Arquitectura en capas
Una petición baja siempre en el mismo orden: **controller → service → repository → model**.

```
src/main/java/com/rutaexpress/shipments/
├── controller/   Solo HTTP: recibe el DTO con @Valid, llama al service y responde
├── service/      Reglas de negocio 
├── repository/   Interfaces de Spring Data (JpaRepository): solo acceso a datos
├── model/        Entidad Shipment (@Entity) y enum ShipmentStatus
├── dto/          Records de entrada y salida, con sus validaciones
├── exception/    Excepciones de negocio y el manejador global 
├── messaging/    Productor RabbitMQ y Kafka
├── config/       Nombres de exchanges y routing keys en un solo lugar
└── client/       Llamadas REST a otros servicios 
```


## Reglas de estado (caso, sección 4.3)
| Desde | Puede pasar a |
|---|---|
| CREADO | ACEPTADO, CANCELADO |
| ACEPTADO | EN_BODEGA, CANCELADO |
| EN_BODEGA | EN_RUTA, CANCELADO |
| EN_RUTA | ENTREGADO |
| ENTREGADO, CANCELADO | — (finales) |

## Tests
```
cd ms-rutaexpress-shipments
mvnw.cmd test
```
