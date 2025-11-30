package com.mercadolibre.mutant_detector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

// Al ser record no es necesario agregar Lombok
public record StatsResponse(
        @Schema(description = "Cantidad de ADN mutante verificado", example = "40")
        @JsonProperty("count_mutant_dna")
        long countMutantDna,

        @Schema(description = "Cantidad de ADN humano verificado", example = "100")
        @JsonProperty("count_human_dna")
        long countHumanDna,

        @Schema(description = "Ratio: mutantes / humanos", example = "0.4")
        double ratio
) {}