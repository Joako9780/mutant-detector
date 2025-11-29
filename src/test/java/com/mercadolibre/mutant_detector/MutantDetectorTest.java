package com.mercadolibre.mutant_detector;

import com.mercadolibre.mutant_detector.detector.MutantDetector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp() {
        mutantDetector = new MutantDetector();
    }

    @Test
    void testIsMutant_NullInput_ThrowsException() {
        // Caso: El array es null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(null);
        });
        assertEquals("El array de ADN no puede ser nulo ni vacío.", exception.getMessage());
    }

    @Test
    void testIsMutant_EmptyInput_ThrowsException() {
        // Caso: El array está vacío
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(new String[]{});
        });
        assertEquals("El array de ADN no puede ser nulo ni vacío.", exception.getMessage());
    }

    @Test
    void testIsMutant_NonSquareMatrix_ThrowsException() {
        // Caso: NxN no se cumple (3 filas, pero cadenas de longitud 4)
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
        // Caso: Contiene una 'X' que no es base nitrogenada
        String[] dna = {
                "ATCG",
                "ATCX", // <--- Caracter inválido
                "ATCG",
                "ATCG"
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
        assertEquals("El ADN contiene caracteres inválidos. Solo se permiten A, T, C, G.", exception.getMessage());
    }

    @Test
    void testIsMutant_NullRow_ThrowsException() {
        // Caso: Una de las filas es null
        String[] dna = {
                "ATCG",
                null,
                "ATCG",
                "ATCG"
        };
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
        assertEquals("La secuencia de ADN no puede ser nula.", exception.getMessage());
    }

    @Test
    void testIsMutant_ValidInput_NoException() {
        // Caso: Input válido (NxN y caracteres correctos).
        // Por ahora devuelve false porque no implementamos la búsqueda.
        String[] dna = {
                "ATCG",
                "CAGT",
                "TTAT",
                "AGAA"
        };
        assertDoesNotThrow(() -> mutantDetector.isMutant(dna));
        assertFalse(mutantDetector.isMutant(dna));
    }
}