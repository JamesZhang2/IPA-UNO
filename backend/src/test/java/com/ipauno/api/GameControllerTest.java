package com.ipauno.api;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.jayway.jsonpath.JsonPath;
import com.ipauno.game.CardView;
import com.ipauno.game.GameService;
import com.ipauno.game.GameView;
import com.ipauno.model.FeatureMatcher;
import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.PulmonicConsonantCatalog;
import java.util.Set;
import java.util.Random;
import java.util.stream.Collectors;
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

        @Test
        void playReturns404() throws Exception {
            mockMvc.perform(
                    post("/api/game/play")
                            .contentType(APPLICATION_JSON)
                            .content("{\"cardId\":\"p#0\"}"))
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

    @Test
    void playReturns400ForUnknownCardWithoutChangingState() throws Exception {
        GameView initialView = startFixedGame();

        mockMvc.perform(
                post("/api/game/play")
                        .contentType(APPLICATION_JSON)
                        .content("{\"cardId\":\"not-a-card\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Unknown card"));

        assertEquals(initialView, gameService.getCurrentGameView().orElseThrow());
    }

    @Test
    void playReturns400ForCardNotInHandWithoutChangingState() throws Exception {
        GameView initialView = startFixedGame();
        Set<String> handIds = initialView.hand()
                .stream()
                .map(CardView::id)
                .collect(Collectors.toSet());
        String cardNotInHand = PulmonicConsonantCatalog.createDeck()
                .stream()
                .map(PulmonicConsonant::id)
                .filter(id -> !handIds.contains(id))
                .findFirst()
                .orElseThrow();

        mockMvc.perform(
                post("/api/game/play")
                        .contentType(APPLICATION_JSON)
                        .content("{\"cardId\":\"%s\"}".formatted(cardNotInHand)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Card not in hand"));

        assertEquals(initialView, gameService.getCurrentGameView().orElseThrow());
    }

    @Test
    void playReturnsDescriptive400ForIllegalMatchWithoutChangingState() throws Exception {
        GameView initialView = startFixedGame();
        CardView illegalCard = initialView.hand()
                .stream()
                .filter(card -> matchingFeatureCount(card, initialView.topCard()) < 2)
                .findFirst()
                .orElseThrow();
        String expectedMessage = FeatureMatcher.explainIllegalPlay(
                toCard(illegalCard),
                toCard(initialView.topCard()));

        mockMvc.perform(
                post("/api/game/play")
                        .contentType(APPLICATION_JSON)
                        .content("{\"cardId\":\"%s\"}".formatted(illegalCard.id())))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(expectedMessage));

        assertEquals(initialView, gameService.getCurrentGameView().orElseThrow());
    }

    @Test
    void playReturns400ForMissingCardIdWithoutChangingState() throws Exception {
        GameView initialView = startFixedGame();

        mockMvc.perform(post("/api/game/play").contentType(APPLICATION_JSON).content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("cardId is required"));

        assertEquals(initialView, gameService.getCurrentGameView().orElseThrow());
    }

    @Test
    void playReturns400ForMissingBodyWithoutChangingState() throws Exception {
        GameView initialView = startFixedGame();

        mockMvc.perform(post("/api/game/play").contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));

        assertEquals(initialView, gameService.getCurrentGameView().orElseThrow());
    }

    @Test
    void playReturns400ForMalformedBodyWithoutChangingState() throws Exception {
        GameView initialView = startFixedGame();

        mockMvc.perform(
                post("/api/game/play")
                        .contentType(APPLICATION_JSON)
                        .content("{not-json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request body is missing or malformed"));

        assertEquals(initialView, gameService.getCurrentGameView().orElseThrow());
    }

    private GameView startFixedGame() {
        return gameService.startNewGame(new Random(42)).toGameView();
    }

    private static PulmonicConsonant toCard(CardView view) {
        return new PulmonicConsonant(
                view.id(),
                view.manner(),
                view.place(),
                view.voicing(),
                view.symbol());
    }

    private static int matchingFeatureCount(CardView first, CardView second) {
        int count = 0;
        count += first.manner() == second.manner() ? 1 : 0;
        count += first.place() == second.place() ? 1 : 0;
        count += first.voicing() == second.voicing() ? 1 : 0;
        return count;
    }
}
