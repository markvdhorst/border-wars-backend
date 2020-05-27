package com.markvdhorst.game.domain;

public class StoneWin {
    private final int stone;
    private final PlayerNumber player;

    public StoneWin(int stone, PlayerNumber player) {
        this.stone = stone;
        this.player = player;
    }

    public int getStone() {
        return stone;
    }

    public PlayerNumber getPlayer() {
        return player;
    }

    @Override
    public String toString() {
        return "StoneWin{" +
                "stone=" + stone +
                ", player=" + player +
                '}';
    }
}
