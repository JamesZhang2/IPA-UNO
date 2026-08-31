package com.ipauno.game;

import com.ipauno.model.PulmonicConsonant;
import com.ipauno.model.PulmonicConsonantCatalog;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class Game {

    private static final int HAND_SIZE = 7;

    private final List<PulmonicConsonant> hand;
    private final Deque<PulmonicConsonant> drawPile;
    private final Deque<PulmonicConsonant> discardPile;
    private GameStatus status;

    private Game(
            List<PulmonicConsonant> hand,
            Deque<PulmonicConsonant> drawPile,
            Deque<PulmonicConsonant> discardPile,
            GameStatus status) {
        this.hand = hand;
        this.drawPile = drawPile;
        this.discardPile = discardPile;
        this.status = status;
    }

    public static Game dealNew(Random random) {
        List<PulmonicConsonant> deck = new ArrayList<>(PulmonicConsonantCatalog.createDeck());
        Collections.shuffle(deck, random);

        List<PulmonicConsonant> hand = new ArrayList<>(deck.subList(0, HAND_SIZE));
        Deque<PulmonicConsonant> discardPile = new ArrayDeque<>();
        discardPile.addLast(deck.get(HAND_SIZE));

        Deque<PulmonicConsonant> drawPile = new ArrayDeque<>(deck.subList(HAND_SIZE + 1, deck.size()));

        return new Game(hand, drawPile, discardPile, GameStatus.IN_PROGRESS);
    }

    public GameStatus status() {
        return status;
    }

    List<PulmonicConsonant> hand() {
        return List.copyOf(hand);
    }

    PulmonicConsonant getDiscardPileTop() {
        return discardPile.peekLast();
    }

    int discardPileSize() {
        return discardPile.size();
    }

    int drawPileCount() {
        return drawPile.size();
    }
}
