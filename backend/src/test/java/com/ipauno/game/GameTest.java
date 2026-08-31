package com.ipauno.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.PulmonicConsonantCatalog;
import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class GameTest {

    private static final int DECK_SIZE = 114;
    private static final int HAND_SIZE = 7;

    @Test
    void newGameDealsSevenCardsToTheHand() {
        Game game = Game.dealNew(new Random(42));

        assertEquals(HAND_SIZE, game.hand().size());
    }

    @Test
    void newGameFlipsOneStarterOntoTheDiscardPile() {
        Game game = Game.dealNew(new Random(42));

        assertEquals(1, game.discardPileSize());
        assertNotNull(game.getDiscardPileTop());
    }

    @Test
    void newGameLeavesRemainingCardsInDrawPile() {
        Game game = Game.dealNew(new Random(42));

        assertEquals(DECK_SIZE - HAND_SIZE - 1, game.drawPileCount());
    }

    @Test
    void newGameAccountsForEveryCardInTheDeck() {
        Game game = Game.dealNew(new Random(42));

        int totalCards = game.hand().size() + game.discardPileSize() + game.drawPileCount();
        assertEquals(DECK_SIZE, totalCards);
    }

    @Test
    void newGameStartsInProgress() {
        Game game = Game.dealNew(new Random(42));

        assertEquals(GameStatus.IN_PROGRESS, game.status());
    }

    @Test
    void dealShufflesCardsBeforeDrawing() {
        List<String> catalogOrder = PulmonicConsonantCatalog.createDeck().stream()
                .map(PulmonicConsonant::id)
                .toList();
        List<String> dealtHand = Game.dealNew(new Random(0)).hand().stream()
                .map(PulmonicConsonant::id)
                .toList();

        assertNotEquals(catalogOrder.subList(0, HAND_SIZE), dealtHand);
    }
}
