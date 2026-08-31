package com.ipauno.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void postStartsGameAndReturnsView() throws Exception {
        mockMvc.perform(post("/api/newGame"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.hand", hasSize(7)))
                .andExpect(jsonPath("$.topCard.id").exists())
                .andExpect(jsonPath("$.drawPileCount").isNumber())
                .andExpect(jsonPath("$.playableCardIds").isArray());
    }

    @Test
    void postReplacesAnExistingGame() throws Exception {
        mockMvc.perform(post("/api/newGame")).andExpect(status().isOk()).andExpect(jsonPath("$.hand", hasSize(7)));

        mockMvc.perform(post("/api/newGame")).andExpect(status().isOk()).andExpect(jsonPath("$.hand", hasSize(7)));
    }
}
