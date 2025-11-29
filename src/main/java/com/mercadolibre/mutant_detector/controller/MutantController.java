package com.mercadolibre.mutant_detector.controller;

import com.mercadolibre.mutant_detector.dto.DnaRequest;
import com.mercadolibre.mutant_detector.service.MutantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mutant")
public class MutantController {

    private final MutantService mutantService;

    public MutantController(MutantService mutantService) {
        this.mutantService = mutantService;
    }

    @PostMapping("/")
    public ResponseEntity<Void> detectMutant(@RequestBody DnaRequest request) {
        try {
            boolean isMutant = mutantService.analyze(request.dna());

            if (isMutant) {
                return ResponseEntity.ok().build(); // HTTP 200
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build(); // HTTP 403
            }
        } catch (IllegalArgumentException e) {
            // Capturamos las validaciones (array vacío, caracteres inválidos, etc.)
            // y devolvemos un 400 Bad Request para ser prolijos.
            return ResponseEntity.badRequest().build();
        }
    }
}