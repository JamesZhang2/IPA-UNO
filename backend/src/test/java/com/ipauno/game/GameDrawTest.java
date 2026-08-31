package com.ipauno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ipauno.model.MannerOfArticulation;
import com.ipauno.model.PlaceOfArticulation;
import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.Voicing;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameDrawTest {

    private static final PulmonicConsonant T =
            card("t#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR, Voicing.VOICELESS, "t");
    private static final PulmonicConsonant P =
            card("p#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL, Voicing.VOICELESS, "p");
    private static final PulmonicConsonant K =
            card("k#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.VELAR, Voicing.VOICELESS, "k");
    private static final PulmonicConsonant G =
            card("g#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.VELAR, Voicing.VOICED, "g");

    @Test
    void drawTakesTopCardIntoHand() {
        Game game = Game.fromState(List.of(P), List.of(K, G), List.of(T), GameStatus.IN_PROGRESS);

        game.draw();

        assertEquals(List.of("p#0", "g#0"), handIds(game));
        assertEquals(1, game.drawPileCount());
    }

    @Test
    void drawReshufflesDiscardExceptTopWhenDrawPileIsEmpty() {
        PulmonicConsonant buriedOne = card("s#0", MannerOfArticulation.FRICATIVE, PlaceOfArticulation.ALVEOLAR,
                Voicing.VOICELESS, "s");
        PulmonicConsonant buriedTwo = card("z#0", MannerOfArticulation.FRICATIVE, PlaceOfArticulation.ALVEOLAR,
                Voicing.VOICED, "z");
        Game game = Game.fromState(
                List.of(P), List.of(), List.of(buriedOne, buriedTwo, T), GameStatus.IN_PROGRESS);

        game.draw();

        assertEquals(2, game.hand().size());
        assertEquals(1, game.discardPileSize());
        assertEquals("t#0", game.getDiscardPileTop().id());
        assertEquals(1, game.drawPileCount());
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
