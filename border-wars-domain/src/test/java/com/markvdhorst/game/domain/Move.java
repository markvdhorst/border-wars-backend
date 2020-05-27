package com.markvdhorst.game.domain;

public class Move {

    private final PlayerNumber player;
    private final Card playedCard;
    private final int stone;

    public Move(PlayerNumber player, Card playedCard, int stone) {
        this.player = player;
        this.playedCard = playedCard;
        this.stone = stone;
    }

    public PlayerNumber getPlayer() {
        return player;
    }

    public Card getPlayedCard() {
        return playedCard;
    }

    public int getStone() {
        return stone;
    }

    @Override
    public String toString() {
        return "Move{" +
                "player=" + player +
                ", playedCard=" + playedCard +
                ", stone=" + stone +
                '}';
    }
}
