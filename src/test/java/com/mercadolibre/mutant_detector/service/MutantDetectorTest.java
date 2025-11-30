package com.mercadolibre.mutant_detector.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // --- 1. CASOS HUMANOS (Return False) ---

    @Test
    @DisplayName("Human: No sequences")
    void testHumanWithNoSequences() {
        String[] dna = {"ATGC", "CAGT", "TTAT", "AGAC"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Human: Only 1 Horizontal sequence")
    void testHumanWithOneHorizontal() {
        String[] dna = {"AAAA", "CAGT", "TTAT", "AGAC"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Human: Only 1 Vertical sequence")
    void testHumanWithOneVertical() {
        String[] dna = {"ATGC", "ATGT", "ATAT", "AGAC"}; // Col 0 is AAAA
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Human: Only 1 Diagonal sequence")
    void testHumanWithOneDiagonal() {
        String[] dna = {"ATGC", "CAGT", "TCAT", "AGAC"}; // Diagonal AAAA
        assertFalse(mutantDetector.isMutant(dna));
    }

    // --- 2. CASOS MUTANTES (Return True) ---

    @Test
    @DisplayName("Mutant: Magneto Example (Diagonal + Vertical)")
    void testMutantMagnetoExample() {
        String[] dna = {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutant: 2 Horizontal Sequences")
    void testMutantTwoHorizontal() {
        String[] dna = {"AAAA", "CCCC", "TCAG", "GGTC"};
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutant: 2 Vertical Sequences")
    void testMutantTwoVertical() {
        String[] dna = {
                "ACGT",
                "ACGT",
                "ACGT",
                "ACGT"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutant: 2 Diagonal Sequences (Main)")
    void testMutantTwoDiagonals() {
        String[] dnaCross = {
                "AAAA",
                "GAGA",
                "GAGA",
                "GAGA"
        };
        assertTrue(mutantDetector.isMutant(dnaCross));
    }

    @Test
    @DisplayName("Mutant: Reverse Diagonal (Counter-Diagonal)")
    void testMutantReverseDiagonal() {
        String[] dna = {
                "AAAG",
                "CCGT",
                "TGTT",
                "GGGG" // Horizontal GGGG + Diagonal inv GGGG
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Mutant: Max Size Matrix (Performance Check)")
    void testMutantLargeMatrix() {
        // Simulamos una matriz más grande llena de A
        // Esto valida que no explote por bounds
        int size = 100;
        String[] dna = new String[size];
        String row = "A".repeat(size);
        for(int i=0; i<size; i++) {
            dna[i] = row;
        }
        assertTrue(mutantDetector.isMutant(dna));
    }

    // --- 3. VALIDACIONES (Exceptions) ---

    @Test
    @DisplayName("Exception: Null Input")
    void testNullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(null));
        assertEquals("El array de ADN no puede ser nulo.", exception.getMessage());
    }

    @Test
    @DisplayName("Exception: Empty Input")
    void testEmptyInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(new String[]{}));
        assertEquals("El array de ADN no puede estar vacío.", exception.getMessage());
    }

    @Test
    @DisplayName("Exception: Non-Square Matrix (NxM)")
    void testNonSquareMatrix() {
        String[] dna = {"ABC", "DEF"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
        assertEquals("La tabla debe ser NxN.", exception.getMessage());
    }

    @Test
    @DisplayName("Exception: Invalid Characters (Numbers)")
    void testInvalidCharactersNumbers() {
        String[] dna = {"1234", "1234", "1234", "1234"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
        assertEquals("Carácter inválido en posición (0,0): 1", exception.getMessage());
    }

    @Test
    @DisplayName("Exception: Null Row")
    void testNullRow() {
        String[] dna = {"AAAA", null, "AAAA", "AAAA"};
        Exception exception = assertThrows(IllegalArgumentException.class, () -> mutantDetector.isMutant(dna));
        assertEquals("La fila 1 no puede ser nula.", exception.getMessage());
    }

    // --- 4. TESTS DE TAMAÑOS VARIADOS (10 tests extra) ---

    @Test
    @DisplayName("Size 1x1: Too small to be mutant")
    void testMatrix1x1() {
        // Imposible encontrar 4 letras en 1x1
        String[] dna = {"A"};
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 3x3: Too small (Human)")
    void testMatrix3x3() {
        // Imposible encontrar 4 letras en 3x3 -> Human
        String[] dna = {
                "AAA",
                "AAA",
                "AAA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 4x4: Smallest Mutant possible (Horiz + Vert)")
    void testMatrix4x4_Mutant() {
        // Mínimo tamaño para ser mutante
        String[] dnaMutant = {
                "AAAA",
                "CCCA",
                "TTTA",
                "GGGA"
        };
        assertTrue(mutantDetector.isMutant(dnaMutant));
    }

    @Test
    @DisplayName("Size 5x5: Mutant with Diagonals")
    void testMatrix5x5_Mutant() {
        String[] dnaMutant = {
                "ATGCG",
                "CATGC",
                "TCATG",
                "GTCAT", // (3,3) es A. Diagonal AAAA completa.
                "CCCCC"  // Horizontal CCCCC completa.
        };
        assertTrue(mutantDetector.isMutant(dnaMutant));
    }

    @Test
    @DisplayName("Size 5x5: Human (Almost mutant)")
    void testMatrix5x5_Human() {
        // Casi tiene secuencias, pero se cortan en 3
        String[] dna = {
                "AAATA",
                "CCCTG",
                "GGGAT",
                "TTTAG",
                "AAAAT" // Esta sí es de 4, pero es solo UNA secuencia.
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 6x6: Sequences at Edges (Corner cases)")
    void testMatrix6x6_Edges() {
        // Probamos secuencias pegadas a los bordes
        String[] dna = {
                "AAAAAA", // Horizontal arriba
                "GGGGGG", // Otra horizontal abajo -> Total 2 secuencias.
                "TTTTTT",
                "CCCCCC",
                "AGTCAG",
                "AGTCAG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 8x8: Mutant with Parallel Verticals")
    void testMatrix8x8_ParallelVerticals() {
        String[] dna = {
                "ATGCATGC",
                "ATGCATGC",
                "ATGCATGC",
                "ATGCATGC", // Aquí ya se formaron verticales:
                            // Col 0 (AAAA), Col 4 (AAAA) -> 2 secuencias
                "CCCCCCCC",
                "CCCCCCCC",
                "CCCCCCCC",
                "CCCCCCCC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 10x10: Human (No sequences)")
    void testMatrix10x10_Human() {
        String[] dna = {
                "ACTGACTGAC",
                "TGCAAGCTGA",
                "ACTGACTGAC",
                "TGCAAGCTGA",
                "ACTGACTGAC",
                "TGCAAGCTGA",
                "ACTGACTGAC",
                "TGCAAGCTGA",
                "ACTGACTGAC",
                "TGCAAGCTGA"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 12x12: Mutant with multiple Crosses")
    void testMatrix12x12_Mutant() {
        // Ponemos una horizontal muy clara al principio y una vertical al final
        String[] dna = new String[12];
        // Llenamos con "basura"
        for(int i=0; i<12; i++) dna[i] = "GTCA".repeat(3);

        // Inyectamos mutaciones
        dna[0] = "AAAAAAAAAAAA"; // 1 secuencia (o más si cuenta distinto, pero al menos 1)
        dna[11] = "CCCCCCCCCCCC"; // 2 secuencias

        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Size 20x20: Performance check (Mutant)")
    void testMatrix20x20_Mutant() {
        // Matriz grandecita
        String[] dna = new String[20];
        // Llenamos con patrón base
        for(int i=0; i<20; i++) dna[i] = "ATGC".repeat(5);

        // Diagonal principal AAAA
        // Modificamos manualmente para formar diagonal
        // Ponemos 2 horizontales claras
        dna[5] = "AAAA" + "ATGC".repeat(4);
        dna[15] = "TTTT" + "ATGC".repeat(4);

        assertTrue(mutantDetector.isMutant(dna));
    }
}