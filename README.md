Microservicio de Clientes - Arquitectura Limpia, Spring WebFlux, JWT y Kafka

Este proyecto es un microservicio reactivo para la gestión de clientes, implementado con Spring WebFlux siguiendo principios de arquitectura limpia. Se incluye autenticación mediante JWT y publicación de eventos en Apache Kafka.

Tecnologías Utilizadas

Java 17

Spring Boot 3.x

Spring WebFlux

MongoDB (Reactive)

Apache Kafka (Reactor Kafka)

MapStruct

JWT (Json Web Token)

Maven

Docker y Docker Compose (para levantar Kafka y MongoDB)

Estructura del Proyecto

El proyecto sigue los principios de arquitectura limpia con las siguientes capas:

com.pierre.clientes
├── application
│   ├── dto
│   ├── mapper
│   └── usecase
├── domain
│   ├── event
│   ├── exception
│   ├── model
│   │   └── shared
│   ├── repository
│   └── util
├── infrastructure
│   ├── config
│   │   ├── kafka
│   │   ├── openapi
│   │   ├── security
|   |   └── web
│   ├── messaging.kafka
│   │   ├── consumer (por implementar)
│   │   └── publisher
│   ├── persistence
│   │   └── mapper
│   ├── rest
│   └── util
├── resources
│   └── application.yml
└── test

Características Implementadas

1. CRUD de Clientes

Crear Cliente: POST /api/clientes

Listar Clientes: GET /api/clientes

Buscar por ID: GET /api/clientes/{id}

2. Seguridad JWT

Filtro personalizado (JwtAuthenticationFilter) que valida el token JWT y propaga el contexto de seguridad.

Clase utilitaria JwtUtil para generar y validar tokens.

Endpoint de login simulado para emitir el token (/api/auth/login).

3. Publicación de Eventos a Kafka

Se define un ReactiveKafkaEventPublisher con KafkaSender para publicar eventos reactivos.

Se envían eventos con headers personalizados (consumerId, traceparent, etc).

Se implementa la serialización segura con JsonUtils.safeWriteASString.

Eventos:

CustomerCreatedEvent: emitido al crear un cliente.

CustomerEvent: emitido al listar clientes (solo un evento por llamada).

4. Manejo de Errores

Validación del token: lanza 401 Unauthorized si es inválido.

Errores en publicación a Kafka: logueados y continúa flujo.

Control de error InvalidCustomerDataException y CustomerNotFoundException donde aplique.

Ejemplo de Headers Enviados en Eventos Kafka

{
  "consumerId": "user123",
  "traceparent": "00-abc123-xyz456-01"
}

Variables de Entorno

Agregar en application.yml:

jwt:
  secret: mysupersecuresecretkeyof32chars!!
  expiration-ms: 3600000

spring:
  data:
    mongodb:
      uri: mongodb://admin:password123@localhost:27017/clientapi?authSource=clientapi

  kafka:
    bootstrap-servers: localhost:9092

topic:
  customer:
    created: customer-created-topic
    consulted: customer-consulted-topic

Docker Compose

Para facilitar el despliegue local de Kafka y MongoDB, se agregará un archivo docker-compose.yml en la raíz del proyecto. Este archivo levantará:

Zookeeper

Apache Kafka

MongoDB

mongo-express (explorador web opcional)

Buenas Prácticas Aplicadas

Separación de responsabilidades por capa.

Programación reactiva y no bloqueante.

Manejo explícito de errores.

Uso de anotaciones modernas (@RequiredArgsConstructor, @Slf4j, etc).

Logs de trazabilidad para Kafka.

Uso de MapStruct para separación de DTOs y lógica de dominio.

Pruebas Unitarias

Incluye pruebas unitarias para los casos de uso:

Crear cliente (CreateCustomerUseCaseTest)

Obtener todos los clientes (GetAllCustomerUseCaseTest)

Obtener cliente por ID (GetCustomerByIdUseCaseTest)

Pendiente / Mejoras Futuras

Validaciones por campo (ej. usando javax.validation).

Tests de integración.

Control de roles/autorización en los endpoints.

Manejo de respuesta para errores 500 con mensajes personalizados.

Implementar consumidor Kafka reactivo.

Autor

Desarrollado por Pierre.
