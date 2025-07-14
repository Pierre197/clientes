package com.pierre.clientes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CustomerRequestDTO (
    @Schema(description = "Nombre del cliente", example = "Pierre")
    @NotBlank(message = "El nombre es obligatorio")
    String firstName,

    @Schema(description = "Apellido paterno", example = "Castillo")
    @NotBlank(message = "El apellido parteno es obligatorio")
    String lastName,

    @Schema(description = "Apellido materno", example = "Llontop")
    @NotNull(message = "El apellido materno no puede ser nulo")
    String secondLastName,

    @Schema(description = "Edad del cliente", example = "35")
    @Min(value = 0, message = "La edad debe ser mayor o igual a 0")
    Integer age,

    @Schema(description = "Fecha de nacimiento (YYYY-MM-DD)", example = "1995-07-14")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    LocalDate birthdate
) {}
