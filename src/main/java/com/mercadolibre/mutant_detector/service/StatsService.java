package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.dto.StatsResponse;
import com.mercadolibre.mutant_detector.repository.DnaRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

    private final DnaRepository dnaRepository;

    public StatsService(DnaRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    public StatsResponse getStats() {
        long mutantCount = dnaRepository.countByIsMutant(true);
        long humanCount = dnaRepository.countByIsMutant(false);

        // Se evita división por cero por si no hay humanos aún
        double ratio = humanCount == 0 ? 0 : (double) mutantCount / humanCount;

        return new StatsResponse(mutantCount, humanCount, ratio);
    }
}