package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.dto.StatsResponse;
import com.mercadolibre.mutant_detector.repository.DnaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private DnaRepository dnaRepository;

    @InjectMocks
    private StatsService statsService;

    @Test
    @DisplayName("Get Stats: Ratio calculation correct")
    void testGetStatsRatio() {
        // 40 Mutantes, 100 Humanos -> Ratio 0.4
        when(dnaRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRepository.countByIsMutant(false)).thenReturn(100L);

        StatsResponse response = statsService.getStats();

        assertEquals(40, response.countMutantDna());
        assertEquals(100, response.countHumanDna());
        assertEquals(0.4, response.ratio());
    }

    @Test
    @DisplayName("Get Stats: No humans -> Ratio 0 (Avoid ArithmeticException)")
    void testGetStatsDivisionByZero() {
        // 10 Mutantes, 0 Humanos
        when(dnaRepository.countByIsMutant(true)).thenReturn(10L);
        when(dnaRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(10, response.countMutantDna());
        assertEquals(0, response.countHumanDna());
        assertEquals(0.0, response.ratio(), "El ratio debería ser 0 si no hay humanos para evitar división por cero");
    }

    @Test
    @DisplayName("Get Stats: Empty DB")
    void testGetStatsEmpty() {
        when(dnaRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse response = statsService.getStats();

        assertEquals(0, response.countMutantDna());
        assertEquals(0, response.countHumanDna());
        assertEquals(0.0, response.ratio());
    }
}