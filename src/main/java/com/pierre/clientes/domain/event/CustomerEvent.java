package com.pierre.clientes.domain.event;

public record CustomerEvent(
        String applicationId,   // ej. "clientes-service"
        String consumerId,      // encabezado X-Consumer-Id
        String customerId,      // UUID generado
        String timestamp,       // ISO-8601 con zona (RFC 3339)
        String traceId,         // W3C traceparent
        String inbound,
        String outbound,
        String transactionCode
) {}