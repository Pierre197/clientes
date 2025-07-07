package com.pierre.clientes.domain.event;

public record CustomerEvent(
        String analyticsTraceSource,
        String applicationId,
        String channelOperationNumber,
        String consumerId,
        String currentDate,
        String customerId,
        String region,
        String statusCode,
        String timestamp,
        String traceId,
        String inbound,
        String outbound,
        String transactionCode
)
    {}
