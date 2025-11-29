package com.mercadolibre.mutant_detector.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record StatsResponse(
        @JsonProperty("count_mutant_dna") long countMutantDna,
        @JsonProperty("count_human_dna") long countHumanDna,
        double ratio
) {
}