package com.ipauno.game;

import static com.ipauno.game.GameTestFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class GameDrawTest {

    @Test
    void drawTakesTopCardIntoHand() {
        Game game = Game.fromState(List.of(P), List.of(K, G), List.of(T), GameStatus.IN_PROGRESS);

        game.draw();

        assertEquals(List.of("p#0", "g#0"), handIds(game));
        assertEquals(1, game.drawPileCount());
    }

    @Test
    void drawReshufflesDiscardExceptTopWhenDrawPileIsEmpty() {
        Game game = Game.fromState(List.of(P), List.of(), List.of(S, Z, T), GameStatus.IN_PROGRESS);

        game.draw();

        assertEquals(2, game.hand().size());
        assertEquals(1, game.discardPileSize());
        assertEquals("t#0", game.getDiscardPileTop().id());
        assertEquals(1, game.drawPileCount());
    }
}
