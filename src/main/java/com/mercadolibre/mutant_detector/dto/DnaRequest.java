package com.mercadolibre.mutant_detector.dto;

// Al ser record no es necesario agregar Lombok
public record DnaRequest(String[] dna) {
}