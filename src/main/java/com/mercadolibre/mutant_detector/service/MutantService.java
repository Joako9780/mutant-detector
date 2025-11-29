package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.detector.MutantDetector;
import org.springframework.stereotype.Service;

@Service
public class MutantService {

    private final MutantDetector mutantDetector;

    // Inyección de dependencias por constructor
    public MutantService(MutantDetector mutantDetector) {
        this.mutantDetector = mutantDetector;
    }

    public boolean analyze(String[] dna) {
        // TODO:
        // Validar y detectar.
        // En el Nivel 3, acá se agerga la lógica para guardar el resultado en BD.
        return mutantDetector.isMutant(dna);
    }
}