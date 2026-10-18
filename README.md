# ms-rutaexpress-shipments

Microservicio de **envíos** del caso RutaExpress (DSY1107 Cloud Native I): CRUD de envíos, cambio de estado con reglas de negocio, persistencia en **Oracle** y **productor RabbitMQ** de los comandos que consume `ms-rutaexpress-notify`.

## Tecnologías
Java 21 · Spring Boot 4.1.1 · Spring Data JPA (Oracle) · Spring AMQP (RabbitMQ) · Bean Validation · Lombok

## Estructura (arquitectura hexagonal)
```
ms-rutaexpress-shipments/src/main/java/com/rutaexpress/shipments/
├── domain/
│   ├── model/       Shipment (agregado), ShipmentStatus (enum + transiciones), ShipmentSearchCriteria
│   └── exception/   ShipmentNotFoundException (404), InvalidStatusTransitionException (409)
├── application/
│   ├── port/in/     ManageShipmentUseCase
│   ├── port/out/    ShipmentRepositoryPort, ShipmentNotificationPort
│   └── service/     ShipmentService  ← decide qué comando publicar según el estado
└── infrastructure/
    ├── config/      RabbitMQConfig   ← nombres centralizados + exchanges del productor
    └── adapter/
        ├── inbound/rest/   ShipmentController, dto/, exception/GlobalExceptionHandler
        └── outbound/
            ├── persistence/  ShipmentPersistenceAdapter, entity/, repository/  (Oracle)
            └── messaging/    RabbitShipmentNotificationAdapter, dto/  (PRODUCTOR)
```

## Endpoints (`/api/v1/shipments`, puerto 8081)
| Método | Ruta | Body / parámetros | Respuesta |
|---|---|---|---|
| POST | `/` | `serviceId, originAddress, destinationAddress, recipientName, recipientEmail` · header opcional `X-User-Id` | 201 · 400 |
| GET | `/` | `?status=&from=yyyy-MM-dd&to=yyyy-MM-dd` (opcionales) | 200 |
| GET | `/{id}` | — | 200 · 404 |
| PUT | `/{id}/status` | `{ "status": "ACEPTADO" }` | 200 · 400 · 404 · 409 |

## Transiciones y mensajes publicados
| Desde | Puede pasar a | Publica |
|---|---|---|
| CREADO | ACEPTADO, CANCELADO | — |
| ACEPTADO | EN_BODEGA, EN_RUTA, CANCELADO | **ACEPTADO**: `cmd.topic` `email.aceptado` + `cmd.topic` `warehouse.ticket.create` |
| EN_BODEGA | EN_RUTA, CANCELADO | **EN_BODEGA**: `cmd.direct` `label.gen` |
| EN_RUTA | ENTREGADO | **EN_RUTA / ENTREGADO**: `cmd.topic` `email.en_ruta` / `email.entregado` |
| ENTREGADO, CANCELADO | — (finales) | **CANCELADO**: `email.cancelado` + `warehouse.ticket.cancel` (si tenía ticket) |

Si RabbitMQ no está disponible, el envío igual se guarda y el fallo queda en el log como `[PUBLICACION-FALLIDA]` (la mensajería no bloquea el core).

## Ejecución local
1. RabbitMQ: `docker compose -f docker-compose.infra.yml up -d` (carpeta Desarrollo).
2. Oracle: desde `ms-rutaexpress-shipments/` → `docker compose up -d ms-shipments-oracle` (usa el `.env` local).
3. `mvnw.cmd spring-boot:run`

## Variables de entorno
| Variable | Por defecto |
|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:oracle:thin:@localhost:1522/ORCLPDB1` |
| `SPRING_DATASOURCE_USERNAME` / `PASSWORD` | `SHIPMENTS_USER` / (ver `.env`) |
| `RABBITMQ_HOST` / `PORT` / `USER` / `PASSWORD` | `localhost` / `5672` / `guest` / `guest` |
| `PORT` | `8081` |
