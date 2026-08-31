package com.ipauno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ipauno.model.FeatureMatcher;
import com.ipauno.model.MannerOfArticulation;
import com.ipauno.model.PlaceOfArticulation;
import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.Voicing;
import java.util.List;
import org.junit.jupiter.api.Test;

class GamePlayTest {

    private static final PulmonicConsonant B =
            card("b#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL, Voicing.VOICED, "b");
    private static final PulmonicConsonant T =
            card("t#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR, Voicing.VOICELESS, "t");
    private static final PulmonicConsonant D =
            card("d#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR, Voicing.VOICED, "d");
    private static final PulmonicConsonant P =
            card("p#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL, Voicing.VOICELESS, "p");

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

        IllegalGameActionException error =
                assertThrows(IllegalGameActionException.class, () -> game.play("d#0"));

        assertEquals("Card not in hand", error.getMessage());
        assertEquals(List.of("p#0"), handIds(game));
        assertEquals("t#0", game.getDiscardPileTop().id());
    }

    @Test
    void playRejectsIllegalFeatureMatchWithDescriptiveMessage() {
        Game game = Game.fromState(List.of(B), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        IllegalGameActionException error =
                assertThrows(IllegalGameActionException.class, () -> game.play("b#0"));

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

    private static List<String> handIds(Game game) {
        return game.hand().stream().map(PulmonicConsonant::id).toList();
    }

    private static PulmonicConsonant card(
            String id,
            MannerOfArticulation manner,
            PlaceOfArticulation place,
            Voicing voicing,
            String symbol) {
        return new PulmonicConsonant(id, manner, place, voicing, symbol);
    }
}
