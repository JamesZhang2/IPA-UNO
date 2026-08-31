package com.ipauno.game;

import static com.ipauno.game.GameTestFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.ipauno.model.MannerOfArticulation;
import com.ipauno.model.PlaceOfArticulation;
import com.ipauno.model.Voicing;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameViewTest {

    @Test
    void viewExposesDrawPileCountButNotDrawPileContents() {
        Game game = Game.fromState(List.of(P), List.of(K, G), List.of(T), GameStatus.IN_PROGRESS);

        GameView view = game.toGameView();

        assertEquals(2, view.drawPileCount());
        assertEquals(List.of("p#0"), handIds(view));
    }

    @Test
    void viewExposesOnlyDiscardTopNotBuriedCards() {
        Game game = Game.fromState(List.of(P), List.of(), List.of(S, T), GameStatus.IN_PROGRESS);

        GameView view = game.toGameView();

        assertEquals("t#0", view.topCard().id());
        assertEquals(2, game.discardPileSize());
    }

    @Test
    void wonGameHasEmptyHand() {
        Game game = Game.fromState(List.of(), List.of(), List.of(T), GameStatus.WON);

        GameView view = game.toGameView();

        assertEquals(GameStatus.WON, view.status());
        assertTrue(view.hand().isEmpty());
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
}
