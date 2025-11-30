package com.mercadolibre.mutant_detector.service;

import org.springframework.stereotype.Component;

@Component
public class MutantDetector {

    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_THRESHOLD = 1;

    public boolean isMutant(String[] dna) {
        // 1. Validaciones básicas rápidas
        if (dna == null) throw new IllegalArgumentException("El array de ADN no puede ser nulo.");
        if (dna.length == 0) throw new IllegalArgumentException("El array de ADN no puede estar vacío.");

        int n = dna.length;
        char[][] matrix = new char[n][n];

        // 2. Conversión y Validación de Caracteres en UN SOLO paso (Optimización de Iteración)
        // Eliminamos el Regex costoso y usamos comparación directa de caracteres.
        for (int i = 0; i < n; i++) {
            String row = dna[i];
            if (row == null) throw new IllegalArgumentException("La fila " + i + " no puede ser nula.");
            if (row.length() != n) throw new IllegalArgumentException("La tabla debe ser NxN.");

            for (int j = 0; j < n; j++) {
                char c = row.charAt(j);
                if (!isValidBase(c)) {
                    throw new IllegalArgumentException("Carácter inválido en posición (" + i + "," + j + "): " + c);
                }
                matrix[i][j] = c;
            }
        }

        int sequenceCount = 0;

        // 3. Búsqueda Optimizada (Single Pass)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Early Termination: Si ya es mutante, cortar inmediatamente.
                if (sequenceCount > MUTANT_THRESHOLD) return true;

                char base = matrix[i][j];

                // HORIZONTAL
                // Solo chequeamos si cabe la secuencia Y si no es continuación de la anterior
                if (j <= n - SEQUENCE_LENGTH) {
                    if (j == 0 || base != matrix[i][j - 1]) {
                        if (checkHorizontal(matrix, i, j)) {
                            sequenceCount++;
                            continue; // Si encontramos horizontal, saltamos para evitar solapamientos
                        }
                    }
                }

                // VERTICAL
                if (i <= n - SEQUENCE_LENGTH) {
                    if (i == 0 || base != matrix[i - 1][j]) {
                        if (checkVertical(matrix, i, j)) {
                            sequenceCount++;
                            continue;
                        }
                    }
                }

                // DIAGONAL PRINCIPAL (\)
                if (i <= n - SEQUENCE_LENGTH && j <= n - SEQUENCE_LENGTH) {
                    if (i == 0 || j == 0 || base != matrix[i - 1][j - 1]) {
                        if (checkDiagonal(matrix, i, j)) {
                            sequenceCount++;
                            continue;
                        }
                    }
                }

                // DIAGONAL SECUNDARIA (/)
                if (i <= n - SEQUENCE_LENGTH && j >= SEQUENCE_LENGTH - 1) {
                    if (i == 0 || j == n - 1 || base != matrix[i - 1][j + 1]) {
                        if (checkAntiDiagonal(matrix, i, j)) {
                            sequenceCount++;
                        }
                    }
                }
            }
        }

        return sequenceCount > MUTANT_THRESHOLD;
    }

    // Validación O(1) sin Regex
    private boolean isValidBase(char c) {
        return c == 'A' || c == 'T' || c == 'C' || c == 'G';
    }

    // --- Optimizaciones de Comparación Directa (Loop Unrolling) ---

    private boolean checkHorizontal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col] == base &&
                matrix[row + 2][col] == base &&
                matrix[row + 3][col] == base;
    }

    private boolean checkDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col + 1] == base &&
                matrix[row + 2][col + 2] == base &&
                matrix[row + 3][col + 3] == base;
    }

    private boolean checkAntiDiagonal(char[][] matrix, int row, int col) {
        char base = matrix[row][col];
        return matrix[row + 1][col - 1] == base &&
                matrix[row + 2][col - 2] == base &&
                matrix[row + 3][col - 3] == base;
    }
}