package com.ipauno.api;

import com.ipauno.game.GameNotFoundException;
import com.ipauno.game.GameService;
import com.ipauno.game.GameView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/newGame")
    public GameView newGame() {
        return gameService.startNewGame().toGameView();
    }

    @GetMapping("/game")
    public GameView getGame() {
        return gameService.getCurrentGameView().orElseThrow(GameNotFoundException::new);
    }

    @PostMapping("/game/draw")
    public GameView draw() {
        return gameService.draw();
    }

    @PostMapping("/game/play")
    public GameView play(@RequestBody PlayCardRequest request) {
        return gameService.play(request.cardId());
    }
}
