package com.mercadolibre.mutant_detector.repository;

import com.mercadolibre.mutant_detector.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRepository extends JpaRepository<DnaRecord, Long> {

    // Busca un registro por su secuencia de ADN
    Optional<DnaRecord> findByDna(String dna);

    // Cuenta cuántos registros hay según si son mutantes o no
    long countByIsMutant(boolean isMutant);
}