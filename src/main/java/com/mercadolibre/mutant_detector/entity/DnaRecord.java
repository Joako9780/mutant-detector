package com.mercadolibre.mutant_detector.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Table(name = "dna_records")
@Data
public class DnaRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El ADN crudo (texto largo)
    @Column(length = 100000)
    private String dna;

    // El Hash SHA-256 (índice rápido y único)
    @Column(unique = true, nullable = false)
    private String dnaHash;

    private boolean isMutant;

    // --- Constructor Vacío (Obligatorio para Hibernate) ---
    public DnaRecord() {
    }

    // --- Constructor para usar en el Service ---
    public DnaRecord(String dna, String dnaHash, boolean isMutant) {
        this.dna = dna;
        this.dnaHash = dnaHash;
        this.isMutant = isMutant;
    }

    // Getter manual para asegurar compatibilidad
    public boolean isMutant() {
        return this.isMutant;
    }
}