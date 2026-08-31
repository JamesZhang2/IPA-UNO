package com.ipauno.game;

import com.ipauno.model.FeatureMatcher;
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
    // The last card in the draw pile is the next card to be drawn.
    private final Deque<PulmonicConsonant> drawPile;
    // The last card in the discard pile is the top card of the discard pile.
    private final Deque<PulmonicConsonant> discardPile;
    private GameStatus status;

    private Game(List<PulmonicConsonant> hand, Deque<PulmonicConsonant> drawPile,
            Deque<PulmonicConsonant> discardPile, GameStatus status) {
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

        Deque<PulmonicConsonant> drawPile = new ArrayDeque<>(
                deck.subList(HAND_SIZE + 1, deck.size()));

        return new Game(hand, drawPile, discardPile, GameStatus.IN_PROGRESS);
    }

    // Package-private test helper
    static Game fromState(
            List<PulmonicConsonant> hand,
            List<PulmonicConsonant> drawPile,
            List<PulmonicConsonant> discardPile,
            GameStatus status) {
        return new Game(
                new ArrayList<>(hand),
                new ArrayDeque<>(drawPile),
                new ArrayDeque<>(discardPile),
                status);
    }

    public void play(String cardId) {
        if (status == GameStatus.WON) {
            throw new IllegalGameActionException("Game is already won");
        }

        PulmonicConsonant cardToPlay = getUniqueCardInHand(cardId);

        PulmonicConsonant target = discardPile.peekLast();
        if (!FeatureMatcher.isLegal(cardToPlay, target)) {
            throw new IllegalGameActionException(
                    FeatureMatcher.explainIllegalPlay(cardToPlay, target));
        }

        hand.remove(cardToPlay);
        discardPile.addLast(cardToPlay);
        if (hand.isEmpty()) {
            status = GameStatus.WON;
        }
    }

    public void draw() {
        if (status == GameStatus.WON) {
            throw new IllegalGameActionException("Game is already won");
        }

        if (drawPile.isEmpty()) {
            reshuffleDiscardIntoDrawPile();
        }

        PulmonicConsonant drawn = drawPile.pollLast();
        if (drawn == null) {
            throw new IllegalGameActionException("Draw pile is empty");
        }

        hand.add(drawn);
    }

    private PulmonicConsonant getUniqueCardInHand(String cardId) {
        PulmonicConsonant match = null;
        for (PulmonicConsonant card : hand) {
            if (!card.id().equals(cardId)) {
                continue;
            }
            if (match != null) {
                throw new IllegalStateException("Duplicate card id in hand: " + cardId);
            }
            match = card;
        }
        if (match == null) {
            throw new IllegalGameActionException("Card not in hand");
        }
        return match;
    }

    private void reshuffleDiscardIntoDrawPile() {
        if (discardPile.size() <= 1) {
            return;
        }

        PulmonicConsonant top = discardPile.removeLast();
        List<PulmonicConsonant> buried = new ArrayList<>(discardPile);
        discardPile.clear();
        discardPile.addLast(top);
        Collections.shuffle(buried);
        drawPile.addAll(buried);
    }

    public GameStatus status() {
        return status;
    }

    public GameView toGameView() {
        PulmonicConsonant top = discardPile.peekLast();
        List<String> playableCardIds = hand.stream()
                .filter(card -> FeatureMatcher.isLegal(card, top))
                .map(PulmonicConsonant::id)
                .toList();

        return new GameView(
                status,
                CardView.from(top),
                hand.stream().map(CardView::from).toList(),
                drawPile.size(),
                playableCardIds);
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
