package com.pierre.clientes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CustomerRequestDTO (
    @Schema(description = "Nombre del cliente", example = "Pierre")
    @NotBlank(message = "El nombre es obligatorio")
    String firstName,

    @Schema(description = "Apellido paterno", example = "Castillo")
    @NotBlank(message = "El apellido parteno es obligatorio")
    String lastName,

    @Schema(description = "Apellido materno", example = "Llontop")
    @NotNull(message = "El apellido materno no puede ser nulo")
    String secondLastName
) {}
