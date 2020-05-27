package com.markvdhorst.game.domain;

public class GameWin {
    private final PlayerNumber player;

    public GameWin(PlayerNumber player) {
        this.player = player;
    }

    public PlayerNumber getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "GameWin{" +
                "player=" + player +
                '}';
    }
}
