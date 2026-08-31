package com.ipauno.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // @SpringBootTest reuses one GameService bean for every test in this class. Other
    // tests call POST /api/newGame, which would leave a game in memory and make a 404
    // assertion fail. Refresh the context so this nested class sees the real initial state.
    @Nested
    @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
    class WhenNoGameStarted {

        @Autowired
        private MockMvc mockMvc;

        @Test
        void getReturns404() throws Exception {
            mockMvc.perform(get("/api/game"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("No game in progress"));
        }
    }

    @Test
    void getReturnsCurrentViewAfterGameStarted() throws Exception {
        mockMvc.perform(post("/api/newGame")).andExpect(status().isOk());

        mockMvc.perform(get("/api/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.hand", hasSize(7)))
                .andExpect(jsonPath("$.topCard.id").exists())
                .andExpect(jsonPath("$.drawPileCount").isNumber());
    }

    @Test
    void postStartsGameAndReturnsView() throws Exception {
        mockMvc.perform(post("/api/newGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.hand", hasSize(7)))
                .andExpect(jsonPath("$.topCard.id").exists())
                .andExpect(jsonPath("$.drawPileCount").isNumber());
    }

    @Test
    void postReplacesAnExistingGame() throws Exception {
        mockMvc.perform(post("/api/newGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hand", hasSize(7)));

        mockMvc.perform(post("/api/newGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hand", hasSize(7)));
    }
}
