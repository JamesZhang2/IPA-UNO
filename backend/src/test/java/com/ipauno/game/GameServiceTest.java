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
}
