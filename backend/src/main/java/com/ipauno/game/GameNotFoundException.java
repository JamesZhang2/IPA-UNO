package com.ipauno.game;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException() {
        super("No game in progress");
    }
}
