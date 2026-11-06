<div align="center">

  <h1>pedidos-service</h1>

  <p align="center">
    <strong>Cristian Camilo Ortiz Sánchez</strong><br />
    DOSW 1 · Semana 14<br />
    Escuela Colombiana de Ingeniería Julio Garavito<br />
    cristian.ortiz-s@mail.escuelaing.edu.co
  </p>

  <p align="center">
    <strong>Arepa Express · API Gateway y RabbitMQ en Azure</strong><br />
    <em>Microservicio que recibe los pedidos y publica el evento a RabbitMQ.</em>
  </p>

</div>

---

## Descripción

- Expone un endpoint REST para crear pedidos y responde de inmediato con estado `RECIBIDO`.
- Al crear el pedido, publica el evento `pedido.creado` en el exchange `arepas.eventos`, sin esperar a la cocina ni a notificaciones.
- Se publica a través del API Gateway bajo el prefijo `/pedidos-api`.

## Tecnologías

- Java 21
- Spring Boot 3.5.9 (Spring Web, Spring for RabbitMQ)
- Maven
- RabbitMQ
- Docker

## Arquitectura (hexagonal)

```
domain/
├─ model/              CrearPedidoRequest, PedidoCreadoEvent
└─ port/
   ├─ in/              CrearPedidoUseCase          «interface»
   └─ out/             PedidoEventPublisher        «interface»
application/
└─ usecase/            CrearPedidoUseCaseImpl
infrastructure/
├─ adapter/
│  ├─ in/rest/         PedidoController
│  └─ out/messaging/   RabbitPedidoEventPublisherImpl
└─ config/             RabbitConfig
```

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| `POST` | `/pedidos` | Crea un pedido y publica el evento |

**Petición:**

```json
{ "cliente": "Laura", "arepas": ["choclo", "huevo"] }
```

**Respuesta (`201 Created`):**

```json
{ "pedidoId": "a1b2c3d4", "estado": "RECIBIDO" }
```

A través del gateway: `POST https://arepas-gw-<alias>.azure-api.net/pedidos-api/pedidos` (requiere `Ocp-Apim-Subscription-Key`).

## Evento publicado

| Exchange | Tipo | Routing key |
|---|---|---|
| `arepas.eventos` | `topic` | `pedido.creado` |

**Contenido (`PedidoCreadoEvent`):**

```json
{ "pedidoId": "a1b2c3d4", "cliente": "Laura", "arepas": ["choclo", "huevo"], "fecha": "2026-11-06T10:30:00" }
```

## Contexto

Forma parte de Arepa Express junto con `menu-service`, `cocina-service` y `notificaciones-service`.
