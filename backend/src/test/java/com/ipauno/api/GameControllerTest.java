package com.ipauno.api;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.ipauno.game.CardView;
import com.ipauno.game.GameService;
import com.ipauno.game.GameView;
import java.util.Random;
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

    @Autowired
    private GameService gameService;

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

        @Test
        void drawReturns404() throws Exception {
            mockMvc.perform(post("/api/game/draw"))
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

    @Test
    void drawReturnsUpdatedView() throws Exception {
        String topCardId = JsonPath.read(
                mockMvc.perform(post("/api/newGame"))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.hand", hasSize(7)))
                        .andExpect(jsonPath("$.drawPileCount").value(106))
                        .andReturn()
                        .getResponse()
                        .getContentAsString(),
                "$.topCard.id");

        mockMvc.perform(post("/api/game/draw"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.hand", hasSize(8)))
                .andExpect(jsonPath("$.topCard.id").value(topCardId))
                .andExpect(jsonPath("$.drawPileCount").value(105));
    }

    @Test
    void playReturnsUpdatedViewForLegalCard() throws Exception {
        GameView initialView = gameService.startNewGame(new Random(42)).toGameView();
        CardView legalCard = initialView.hand()
                .stream()
                .filter(card -> matchingFeatureCount(card, initialView.topCard()) >= 2)
                .findFirst()
                .orElseThrow();

        mockMvc.perform(
                post("/api/game/play")
                        .contentType(APPLICATION_JSON)
                        .content("{\"cardId\":\"%s\"}".formatted(legalCard.id())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"))
                .andExpect(jsonPath("$.hand", hasSize(6)))
                .andExpect(jsonPath("$.topCard.id").value(legalCard.id()))
                .andExpect(jsonPath("$.drawPileCount").value(initialView.drawPileCount()));
    }

    private static int matchingFeatureCount(CardView first, CardView second) {
        int count = 0;
        count += first.manner() == second.manner() ? 1 : 0;
        count += first.place() == second.place() ? 1 : 0;
        count += first.voicing() == second.voicing() ? 1 : 0;
        return count;
    }
}
