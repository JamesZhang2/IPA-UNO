package com.ipauno.game;

import static com.ipauno.game.GameTestFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ipauno.model.FeatureMatcher;
import java.util.List;
import org.junit.jupiter.api.Test;

class GamePlayTest {

    @Test
    void playMovesLegalCardFromHandToDiscard() {
        Game game = Game.fromState(List.of(D, P), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        game.play("d#0");

        assertEquals(List.of("p#0"), handIds(game));
        assertEquals("d#0", game.getDiscardPileTop().id());
        assertEquals(GameStatus.IN_PROGRESS, game.status());
    }

    @Test
    void playRejectsCardNotInHand() {
        Game game = Game.fromState(List.of(P), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        IllegalGameActionException error = assertThrows(
                IllegalGameActionException.class,
                () -> game.play("d#0"));

        assertEquals("Card not in hand", error.getMessage());
        assertEquals(List.of("p#0"), handIds(game));
        assertEquals("t#0", game.getDiscardPileTop().id());
    }

    @Test
    void playRejectsIllegalFeatureMatchWithDescriptiveMessage() {
        Game game = Game.fromState(List.of(B), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        IllegalGameActionException error = assertThrows(
                IllegalGameActionException.class,
                () -> game.play("b#0"));

        assertEquals(FeatureMatcher.explainIllegalPlay(B, T), error.getMessage());
        assertEquals(List.of("b#0"), handIds(game));
        assertEquals("t#0", game.getDiscardPileTop().id());
    }

    @Test
    void playWinsWhenHandBecomesEmpty() {
        Game game = Game.fromState(List.of(D), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        game.play("d#0");

        assertEquals(List.of(), handIds(game));
        assertEquals(GameStatus.WON, game.status());
    }
}
