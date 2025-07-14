package com.pierre.clientes.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record CustomerResponseDTO(

        @Schema(description = "Id del cliente", example = "f1234abc-5678-def9-0123-456789abcdef")
        String id,

        @Schema(description = "Nombre completo del cliente", example = "Pierre Castillo Llontop")
        String fullName,

        @Schema(description = "Edad del cliente", example = "30")
        Integer age,

        @Schema(description = "Fecha de nacimiento del cliente", example = "1989-07-19")
        LocalDate birthdate,

        @Schema(description = "Fecha estimada de esperanza de vida", example = "2074-07-14")
        LocalDate estimatedLifeExpectancyDate

) {}