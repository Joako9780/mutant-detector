package com.mercadolibre.mutant_detector.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.io.Serializable;

@Entity
@Data
public class DnaRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 100000)
    private String dna;

    private boolean isMutant;

    // --- CONSTRUCTOR VACÍO (Requerido por Hibernate) ---
    public DnaRecord() {
    }

    // Constructor con argumentos
    public DnaRecord(String dna, boolean isMutant) {
        this.dna = dna;
        this.isMutant = isMutant;
    }

    // Getter manual para asegurar compatibilidad
    public boolean isMutant() {
        return this.isMutant;
    }
}