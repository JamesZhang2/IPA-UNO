package com.ipauno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameServiceTest {

    @Test
    void startNewGameStoresTheCurrentGame() {
        GameService service = new GameService();

        Game game = service.startNewGame(new Random(42));

        assertEquals(Optional.of(game), service.getCurrentGame());
    }

    @Test
    void startNewGameReplacesAnExistingGame() {
        GameService service = new GameService();
        Game first = service.startNewGame(new Random(1));
        Game second = service.startNewGame(new Random(2));

        assertEquals(Optional.of(second), service.getCurrentGame());
        assertTrue(first != second);
    }

    @Test
    void drawUpdatesAndReturnsTheCurrentGameView() {
        GameService service = new GameService();
        Game game = service.startNewGame(new Random(42));

        GameView view = service.draw();

        assertEquals(8, view.hand().size());
        assertEquals(105, view.drawPileCount());
        assertEquals(game.toGameView(), view);
    }

    @Test
    void drawThrowsWhenNoGameHasStarted() {
        GameService service = new GameService();

        GameNotFoundException exception = assertThrows(GameNotFoundException.class, service::draw);

        assertEquals("No game in progress", exception.getMessage());
    }

    @Test
    void playUpdatesAndReturnsTheCurrentGameView() {
        GameService service = new GameService();
        Game game = service.startNewGame(new Random(42));
        GameView initialView = game.toGameView();
        CardView legalCard = initialView.hand()
                .stream()
                .filter(card -> matchingFeatureCount(card, initialView.topCard()) >= 2)
                .findFirst()
                .orElseThrow();

        GameView view = service.play(legalCard.id());

        assertEquals(6, view.hand().size());
        assertEquals(legalCard, view.topCard());
        assertEquals(initialView.drawPileCount(), view.drawPileCount());
        assertEquals(game.toGameView(), view);
    }

    private static int matchingFeatureCount(CardView first, CardView second) {
        int count = 0;
        count += first.manner() == second.manner() ? 1 : 0;
        count += first.place() == second.place() ? 1 : 0;
        count += first.voicing() == second.voicing() ? 1 : 0;
        return count;
    }
}
