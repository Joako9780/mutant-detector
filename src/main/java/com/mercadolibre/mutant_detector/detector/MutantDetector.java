package com.mercadolibre.mutant_detector.detector;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class MutantDetector {

    // Expresión regular para validar que solo contenga A, T, C, G
    private static final Pattern VALID_DNA_PATTERN = Pattern.compile("[ATCG]+");

    public boolean isMutant(String[] dna) {
        // 1. Validar input antes de procesar
        validateInput(dna);

        // TODO: Acá se implementará la lógica de búsqueda (Horizontal, Vertical, Oblicua)

        return false; // No es mutante hasta probar lo contrario
    }

    private void validateInput(String[] dna) {
        // Validación 1: No nulo ni vacío
        if (dna == null || dna.length == 0) {
            throw new IllegalArgumentException("El array de ADN no puede ser nulo ni vacío.");
        }

        int n = dna.length;

        for (String sequence : dna) {
            // Validación 2: Filas no nulas
            if (sequence == null) {
                throw new IllegalArgumentException("La secuencia de ADN no puede ser nula.");
            }

            // Validación 3: Matriz Cuadrada (NxN)
            if (sequence.length() != n) {
                throw new IllegalArgumentException("La tabla de ADN debe ser cuadrada (NxN).");
            }

            // Validación 4: Caracteres permitidos
            if (!VALID_DNA_PATTERN.matcher(sequence).matches()) {
                throw new IllegalArgumentException("El ADN contiene caracteres inválidos. Solo se permiten A, T, C, G.");
            }
        }
    }
}