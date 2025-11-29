package com.mercadolibre.mutant_detector.detector;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class MutantDetector {

    // Expresión regular para validar que solo contenga A, T, C, G
    private static final Pattern VALID_DNA_PATTERN = Pattern.compile("[ATCG]+");
    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_THRESHOLD = 1; // Más de 1 secuencia

    public boolean isMutant(String[] dna) {
        validateInput(dna);

        int n = dna.length;
        char[][] matrix = new char[n][n];

        // Convertimos a matriz de caracteres para acceso rápido
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }

        int sequenceCount = 0;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Optimización: Si ya encontramos suficientes secuencias, cortamos temprano.
                if (sequenceCount > MUTANT_THRESHOLD) {
                    return true;
                }

                // 1. Horizontal: Verificamos solo si no es continuación de la anterior
                if (j <= n - SEQUENCE_LENGTH) {
                    if (j == 0 || matrix[i][j] != matrix[i][j - 1]) {
                        if (checkSequence(matrix, i, j, 0, 1)) {
                            sequenceCount++;
                            continue; // Si encontramos una, pasamos al siguiente check para no solapar
                        }
                    }
                }

                // 2. Vertical: Verificamos solo si no es continuación de la superior
                if (i <= n - SEQUENCE_LENGTH) {
                    if (i == 0 || matrix[i][j] != matrix[i - 1][j]) {
                        if (checkSequence(matrix, i, j, 1, 0)) {
                            sequenceCount++;
                            continue;
                        }
                    }
                }

                // 3. Oblicua (Diagonal Principal): Verificamos si no es continuación
                if (i <= n - SEQUENCE_LENGTH && j <= n - SEQUENCE_LENGTH) {
                    if (i == 0 || j == 0 || matrix[i][j] != matrix[i - 1][j - 1]) {
                        if (checkSequence(matrix, i, j, 1, 1)) {
                            sequenceCount++;
                            continue;
                        }
                    }
                }

                // 4. Oblicua Inversa (Diagonal Secundaria)
                if (i <= n - SEQUENCE_LENGTH && j >= SEQUENCE_LENGTH - 1) {
                    if (i == 0 || j == n - 1 || matrix[i][j] != matrix[i - 1][j + 1]) {
                        if (checkSequence(matrix, i, j, 1, -1)) {
                            sequenceCount++;
                        }
                    }
                }
            }
        }

        return sequenceCount > MUTANT_THRESHOLD;
    }

    /**
     * Verifica si existe una secuencia de 4 caracteres iguales en una dirección dada.
     * @param matrix La matriz de ADN
     * @param row Fila inicial
     * @param col Columna inicial
     * @param dRow Delta fila (cambio en fila por paso)
     * @param dCol Delta columna (cambio en columna por paso)
     */
    private boolean checkSequence(char[][] matrix, int row, int col, int dRow, int dCol) {
        char startChar = matrix[row][col];
        for (int k = 1; k < SEQUENCE_LENGTH; k++) {
            if (matrix[row + k * dRow][col + k * dCol] != startChar) {
                return false;
            }
        }
        return true;
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