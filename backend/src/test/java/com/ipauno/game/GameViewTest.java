package com.ipauno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ipauno.model.FeatureMatcher;
import com.ipauno.model.MannerOfArticulation;
import com.ipauno.model.PlaceOfArticulation;
import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.Voicing;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameViewTest {

    private static final PulmonicConsonant B =
            card("b#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL, Voicing.VOICED, "b");
    private static final PulmonicConsonant T =
            card("t#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR, Voicing.VOICELESS, "t");
    private static final PulmonicConsonant D =
            card("d#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.ALVEOLAR, Voicing.VOICED, "d");
    private static final PulmonicConsonant P =
            card("p#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.BILABIAL, Voicing.VOICELESS, "p");
    private static final PulmonicConsonant K =
            card("k#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.VELAR, Voicing.VOICELESS, "k");
    private static final PulmonicConsonant G =
            card("g#0", MannerOfArticulation.PLOSIVE, PlaceOfArticulation.VELAR, Voicing.VOICED, "g");

    @Test
    void viewExposesDrawPileCountButNotDrawPileContents() {
        Game game = Game.fromState(List.of(P), List.of(K, G), List.of(T), GameStatus.IN_PROGRESS);

        GameView view = game.toGameView();

        assertEquals(2, view.drawPileCount());
        assertEquals(List.of("p#0"), handIds(view));
    }

    @Test
    void viewExposesOnlyDiscardTopNotBuriedCards() {
        PulmonicConsonant buried = card("s#0", MannerOfArticulation.FRICATIVE, PlaceOfArticulation.ALVEOLAR,
                Voicing.VOICELESS, "s");
        Game game = Game.fromState(List.of(P), List.of(), List.of(buried, T), GameStatus.IN_PROGRESS);

        GameView view = game.toGameView();

        assertEquals("t#0", view.topCard().id());
        assertEquals(2, game.discardPileSize());
    }

    @Test
    void playableCardIdsMatchFeatureMatcher() {
        Game game = Game.fromState(List.of(D, B), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        GameView view = game.toGameView();

        assertEquals(
                game.hand().stream()
                        .filter(card -> FeatureMatcher.isLegal(card, T))
                        .map(PulmonicConsonant::id)
                        .toList(),
                view.playableCardIds());
        assertEquals(List.of("d#0"), view.playableCardIds());
    }

    @Test
    void wonGameHasEmptyHand() {
        Game game = Game.fromState(List.of(), List.of(), List.of(T), GameStatus.WON);

        GameView view = game.toGameView();

        assertEquals(GameStatus.WON, view.status());
        assertTrue(view.hand().isEmpty());
        assertTrue(view.playableCardIds().isEmpty());
    }

    @Test
    void cardViewIncludesFullFeaturesAndSymbol() {
        Game game = Game.fromState(List.of(P), List.of(), List.of(T), GameStatus.IN_PROGRESS);

        CardView topCard = game.toGameView().topCard();

        assertEquals("t#0", topCard.id());
        assertEquals("t", topCard.symbol());
        assertEquals(MannerOfArticulation.PLOSIVE, topCard.manner());
        assertEquals(PlaceOfArticulation.ALVEOLAR, topCard.place());
        assertEquals(Voicing.VOICELESS, topCard.voicing());
    }

    private static List<String> handIds(GameView view) {
        return view.hand().stream().map(CardView::id).toList();
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
