package com.pierre.clientes.domain.model.shared;

public record RequestHeaders(String consumerId, String traceparent, String deviceType, String deviceId) { }
