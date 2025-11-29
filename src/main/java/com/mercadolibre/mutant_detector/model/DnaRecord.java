package com.mercadolibre.mutant_detector.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
public class DnaRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true) // unique para que la base de datos rechace duplicados
    private String dna;

    private boolean isMutant;

    // Constructor sin ID
    public DnaRecord(String dna, boolean isMutant) {
        this.dna = dna;
        this.isMutant = isMutant;
    }
}