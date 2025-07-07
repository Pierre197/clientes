package com.pierre.clientes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record CustomerResponseDTO (
        @Schema(description = "Id del cliente")
        String id,

        @Schema(description = "Nombre completo del cliente")
        String fullName) {}