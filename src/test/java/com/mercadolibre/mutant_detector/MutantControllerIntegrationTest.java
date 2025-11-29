package com.mercadolibre.mutant_detector;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest     //levanta el contexto completo de la aplicación
@AutoConfigureMockMvc
class MutantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testDetectMutant_Returns200() throws Exception {
        // Caso Mutante
        String mutantJson = """
            {
                "dna": [
                    "ATGCGA",
                    "CAGTGC",
                    "TTATGT",
                    "AGAAGG",
                    "CCCCTA",
                    "TCACTG"
                ]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mutantJson))
                .andExpect(status().isOk()); // Respuesta 200 OK
    }

    @Test
    void testDetectHuman_Returns403() throws Exception {
        // Caso Humano
        String humanJson = """
            {
                "dna": [
                    "ATGCGA",
                    "CAGTGC",
                    "TTATTT",
                    "AGACGG",
                    "GCGTCA",
                    "TCACTG"
                ]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(humanJson))
                .andExpect(status().isForbidden()); // Respuesta 403 Forbidden
    }

    @Test
    void testInvalidDna_Returns400() throws Exception {
        // Caso Inválido (Array vacío o caracteres extraños)
        // Esto prueba que el 'try-catch' del Controller funciona
        String invalidJson = """
            {
                "dna": []
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest()); // Respuesta 400 Bad Request
    }

    @Test
    void testInvalidCharacters_Returns400() throws Exception {
        // Caso con caracteres no válidos (X)
        String invalidCharJson = """
            {
                "dna": [
                    "ATGCGNc",
                    "CAGTGC",
                    "TTATGT",
                    "AGAAGG",
                    "CCCCTA",
                    "TCACTG"
                ]
            }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidCharJson))
                .andExpect(status().isBadRequest());
    }
}