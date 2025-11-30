package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.entity.DnaRecord;
import com.mercadolibre.mutant_detector.repository.DnaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MutantService {

    private final MutantDetector mutantDetector;
    private final DnaRepository dnaRepository;

    public MutantService(MutantDetector mutantDetector, DnaRepository dnaRepository) {
        this.mutantDetector = mutantDetector;
        this.dnaRepository = dnaRepository;
    }

    @Transactional
    public boolean analyze(String[] dna) {
        // 1. Array a String para la base de datos
        String dnaString = String.join(",", dna);

        // 2. Verificación de si ya existe en BD
        Optional<DnaRecord> existingRecord = dnaRepository.findByDna(dnaString);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        // 3. Si no existe, calculamos
        boolean isMutant = mutantDetector.isMutant(dna);

        // 4. Guardar resultado
        DnaRecord newRecord = new DnaRecord(dnaString, isMutant);
        dnaRepository.save(newRecord);

        return isMutant;
    }
}