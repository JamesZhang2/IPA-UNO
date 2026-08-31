package com.ipauno.game;

import java.util.Optional;
import java.util.Random;
import org.springframework.stereotype.Service;

@Service
public class GameService {

    private Game currentGame;

    public Game startNewGame() {
        return startNewGame(new Random());
    }

    public Game startNewGame(Random random) {
        currentGame = Game.dealNew(random);
        return currentGame;
    }

    public Optional<Game> getCurrentGame() {
        return Optional.ofNullable(currentGame);
    }

    public Optional<GameView> getCurrentGameView() {
        return getCurrentGame().map(Game::toGameView);
    }
}
