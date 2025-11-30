package com.mercadolibre.mutant_detector;

import com.mercadolibre.mutant_detector.service.MutantDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    // --- TESTS DE VALIDACIÓN (Sad Paths) ---

    @Test
    void testIsMutant_NullInput_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(null);
        });
        assertEquals("El array de ADN no puede ser nulo ni vacío.", exception.getMessage());
    }

    @Test
    void testIsMutant_EmptyInput_ThrowsException() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(new String[]{});
        });
        assertEquals("El array de ADN no puede ser nulo ni vacío.", exception.getMessage());
    }

    @Test
    void testIsMutant_NonSquareMatrix_ThrowsException() {
        String[] dna = {
                "ATCG",
                "ATCG",
                "ATCG"
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
        assertEquals("La tabla de ADN debe ser cuadrada (NxN).", exception.getMessage());
    }

    @Test
    void testIsMutant_InvalidCharacters_ThrowsException() {
        String[] dna = {
                "ATCG",
                "ATCX", // X es inválido
                "ATCG",
                "ATCG"
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
        assertEquals("El ADN contiene caracteres inválidos. Solo se permiten A, T, C, G.", exception.getMessage());
    }

    // --- TESTS DE LÓGICA DE NEGOCIO (Happy Paths) ---

    @Test
    void testIsMutant_MagnetoExample_ReturnsTrue() {
        // Ejemplo del PDF: Debería ser Mutante
        // Contiene:
        // - Diagonal Principal: AAAA (desde 0,0)
        // - Horizontal: CCCC (en fila 4)
        // - Vertical: (Opcional, con 2 ya basta para ser true)
        String[] dna = {
                "ATGCGA",
                "CAGTGC",
                "TTATGT",
                "AGAAGG",
                "CCCCTA",
                "TCACTG"
        };
        assertTrue(mutantDetector.isMutant(dna), "El ADN de Magneto debería ser detectado como mutante.");
    }

    @Test
    void testIsMutant_Human_ReturnsFalse() {
        // Caso básico de Humano: Sin secuencias de 4 letras repetidas
        String[] dna = {
                "ATGC",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna), "Un ADN sin secuencias repetidas debería ser humano.");
    }

    @Test
    void testIsMutant_OnlyOneSequence_ReturnsFalse() {
        // Caso Borde: Solo una secuencia (Horizontal AAAA).
        // El requerimiento dice "más de una secuencia", es decir, > 1.
        String[] dna = {
                "AAAA", // 1 secuencia
                "CAGT",
                "TTAT",
                "AGAC"
        };
        // Si hay solo 1, no es mutante según la regla "más de una secuencia"
        assertFalse(mutantDetector.isMutant(dna), "Solo una secuencia no debería ser suficiente para ser mutante.");
    }

    @Test
    void testIsMutant_CrossedSequences_ReturnsTrue() {
        // Caso complejo: Una fila y una columna se cruzan
        // Fila 0: AAAA
        // Columna 0: A, G, G, G (no) -> Hagamos Columna 2: A, A, A, A
        String[] dna = {
                "AAAA", // Horizontal
                "GAGA",
                "GAGA",
                "GAGA"  // Vertical en columna 1 y columna 3
        };
        // Aquí hay:
        // 1. Horizontal en fila 0 (AAAA)
        // 2. Vertical en Columna 1 (AAAA)
        // 3. Vertical en Columna 3 (AAAA)
        // Total 3 secuencias -> Mutante
        assertTrue(mutantDetector.isMutant(dna));
    }
}