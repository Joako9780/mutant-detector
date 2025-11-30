package com.mercadolibre.mutant_detector.controller;

import com.mercadolibre.mutant_detector.dto.DnaRequest;
import com.mercadolibre.mutant_detector.dto.StatsResponse;
import com.mercadolibre.mutant_detector.service.MutantService;
import com.mercadolibre.mutant_detector.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Mutant Detector", description = "Operaciones para detección y estadísticas de mutantes")
public class MutantController {

    private final MutantService mutantService;
    private final StatsService statsService;

    public MutantController(MutantService mutantService, StatsService statsService) {
        this.mutantService = mutantService;
        this.statsService = statsService;
    }

    @Operation(summary = "Detectar mutante", description = "Analiza una secuencia de ADN para determinar si es mutante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Es Mutante"),
            @ApiResponse(responseCode = "403", description = "Es Humano (Forbidden)"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/mutant/")
    public ResponseEntity<Void> detectMutant(@Valid @RequestBody DnaRequest request) {
        boolean isMutant = mutantService.analyze(request.dna());
        if (isMutant) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @Operation(summary = "Obtener estadísticas", description = "Devuelve el conteo de humanos, mutantes y el ratio.")
    @ApiResponse(responseCode = "200", description = "Estadísticas obtenidas correctamente")
    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats() {
        return ResponseEntity.ok(statsService.getStats());
    }
}