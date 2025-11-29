package com.mercadolibre.mutant_detector;

import com.mercadolibre.mutant_detector.repository.DnaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MutantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    // Inyección del repo para limpiar la base de datos
    @Autowired
    private DnaRepository dnaRepository;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        dnaRepository.deleteAll();
    }

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
        // Caso Inválido (Array vacío)
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

    // test para estadísticas
    @Test
    void testGetStats_ReturnsCorrectJson() throws Exception {
        // 1. Insertamos un MUTANTE
        String mutantJson = """
            { "dna": ["AAAA", "CCCC", "TCAG", "GGTC"] }
            """;

        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mutantJson))
                .andExpect(status().isOk());

        // 2. Insertamos un HUMANO
        String humanJson = """
            { "dna": ["AAAT", "CAGT", "TTAT", "AGAC"] }
            """;
        mockMvc.perform(post("/mutant/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(humanJson))
                .andExpect(status().isForbidden());

        // 3. Consultamos STATS
        mockMvc.perform(get("/stats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count_mutant_dna", is(1)))
                .andExpect(jsonPath("$.count_human_dna", is(1)))
                .andExpect(jsonPath("$.ratio", is(1.0)));
    }
}