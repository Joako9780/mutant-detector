package com.mercadolibre.mutant_detector.service;

import com.mercadolibre.mutant_detector.entity.DnaRecord;
import com.mercadolibre.mutant_detector.repository.DnaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
        String dnaString = String.join(",", dna);

        // 1. Calculamos el Hash del ADN (SHA-256)
        String dnaHash = calculateHash(dnaString);

        // 2. Verificamos si ya existe usando el Hash (Búsqueda O(1))
        Optional<DnaRecord> existingRecord = dnaRepository.findByDnaHash(dnaHash);
        if (existingRecord.isPresent()) {
            return existingRecord.get().isMutant();
        }

        // 3. Si no existe, calculamos
        boolean isMutant = mutantDetector.isMutant(dna);

        // 4. Guardamos con el Hash
        DnaRecord newRecord = new DnaRecord(dnaString, dnaHash, isMutant);
        dnaRepository.save(newRecord);

        return isMutant;
    }

    // Metodo auxiliar para generar SHA-256
    private String calculateHash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convertir bytes a Hexadecimal
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error inicializando SHA-256", e);
        }
    }
}