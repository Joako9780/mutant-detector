package com.mercadolibre.mutant_detector.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

// Al ser record no es necesario agregar Lombok
public record DnaRequest(
        @Schema(
                description = "Secuencia de ADN representada como matriz NxN",
                example = "[\"ATGCGA\",\"CAGTGC\",\"TTATGT\",\"AGAAGG\",\"CCCCTA\",\"TCACTG\"]"
        )
        @NotNull(message = "El ADN no puede ser nulo")
        @NotEmpty(message = "El ADN no puede estar vacío")
        String[] dna
) {}