package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.service.MutantDetector;
import com.mercadolibre.mutant_detector.entity.DnaRecord;
import com.mercadolibre.mutant_detector.repository.DnaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private MutantService mutantService;

    private String[] dnaMutant;
    private String dnaString;

    @BeforeEach
    void setUp() {
        dnaMutant = new String[]{"AAAA", "CCCC", "TCAG", "GGTC"};
        dnaString = "AAAA,CCCC,TCAG,GGTC";
    }

    @Test
    @DisplayName("Analyze: New DNA (Mutant) -> Detects and Saves")
    void testAnalyzeNewMutant() {
        // 1. Mock: No existe en BD (buscamos por hash)
        // Usamos anyString() porque el hash se calcula dentro del método analyze
        when(dnaRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());

        // 2. Mock: Es mutante según el detector
        when(mutantDetector.isMutant(dnaMutant)).thenReturn(true);

        // Ejecutar
        boolean result = mutantService.analyze(dnaMutant);

        // Verificar
        assertTrue(result);
        verify(mutantDetector, times(1)).isMutant(dnaMutant);
        // Verificamos que guarde cualquier DnaRecord (el hash interno no nos importa validar aquí)
        verify(dnaRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Analyze: New DNA (Human) -> Detects and Saves")
    void testAnalyzeNewHuman() {
        // 1. Mock: No existe en BD
        when(dnaRepository.findByDnaHash(anyString())).thenReturn(Optional.empty());
        // 2. Mock: NO es mutante
        when(mutantDetector.isMutant(dnaMutant)).thenReturn(false);

        boolean result = mutantService.analyze(dnaMutant);

        assertFalse(result);
        verify(dnaRepository, times(1)).save(argThat(record -> !record.isMutant()));
    }

    @Test
    @DisplayName("Analyze: Existing DNA -> Returns Cached Result (No detection)")
    void testAnalyzeExistingDna() {
        // 1. Mock: YA existe en BD
        // Actualizamos el constructor: (dna, hash, isMutant)
        DnaRecord existingRecord = new DnaRecord(dnaString, "fakeHash123", true);

        when(dnaRepository.findByDnaHash(anyString())).thenReturn(Optional.of(existingRecord));

        // Ejecutar
        boolean result = mutantService.analyze(dnaMutant);

        // Verificar
        assertTrue(result);
        verify(mutantDetector, never()).isMutant(any()); // ¡IMPORTANTE! No llama al detector
        verify(dnaRepository, never()).save(any()); // No guarda de nuevo
    }
}