package com.ipauno.game;

import java.util.List;

public record GameView(
        GameStatus status,
        CardView topCard,
        List<CardView> hand,
        int drawPileCount,
        List<String> playableCardIds) {
}
