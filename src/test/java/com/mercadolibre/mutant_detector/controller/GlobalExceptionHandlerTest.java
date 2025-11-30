package com.mercadolibre.mutant_detector.controller;

import com.mercadolibre.mutant_detector.service.MutantService;
import com.mercadolibre.mutant_detector.service.StatsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MutantController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mockeamos los servicios para forzar errores sin lógica real
    @MockBean
    private MutantService mutantService;

    @MockBean
    private StatsService statsService;

    @Test
    @DisplayName("Handle Validation Error (400 Bad Request)")
    void testHandleValidationErrors() throws Exception {
        // Enviamos un JSON inválido (array vacío) para disparar @Valid
        String invalidJson = """
            { "dna": [] }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                // El mensaje exacto depende de tu DTO, verificamos que exista
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("Handle Business Logic Error (400 Bad Request)")
    void testHandleBusinessException() throws Exception {
        // Simulamos que el servicio lanza IllegalArgumentException
        when(mutantService.analyze(any())).thenThrow(new IllegalArgumentException("ADN inválido detectado"));

        String validJson = """
            { "dna": ["AAAA", "CCCC", "GGGG", "TTTT"] }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.message", is("ADN inválido detectado")));
    }

    @Test
    @DisplayName("Handle Unexpected Error (500 Internal Server Error)")
    void testHandleGlobalException() throws Exception {
        // Simulamos un error grave inesperado (ej. NullPointerException, DB caída)
        when(mutantService.analyze(any())).thenThrow(new RuntimeException("Error fatal en base de datos"));

        String validJson = """
            { "dna": ["AAAA", "CCCC", "GGGG", "TTTT"] }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validJson))
                .andExpect(status().isInternalServerError()) // Esperamos 500
                .andExpect(jsonPath("$.error", is("Internal Server Error")))
                .andExpect(jsonPath("$.message", is("Ocurrió un error inesperado. Contacte al administrador.")));
    }
}