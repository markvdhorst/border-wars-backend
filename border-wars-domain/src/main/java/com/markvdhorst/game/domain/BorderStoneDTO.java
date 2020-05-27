package com.markvdhorst.game.domain;

import java.util.List;
import java.util.Map;

public class BorderStoneDTO {
    private Map<PlayerNumber, List<Card>> playedCards;
    private PlayerNumber firstCompletion;
    private int stoneNumber;

    public BorderStoneDTO(Map<PlayerNumber, List<Card>> playedCards, PlayerNumber firstCompletion, int stoneNumber) {
        this.playedCards = playedCards;
        this.firstCompletion = firstCompletion;
        this.stoneNumber = stoneNumber;
    }

    public Map<PlayerNumber, List<Card>> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(Map<PlayerNumber, List<Card>> playedCards) {
        this.playedCards = playedCards;
    }

    public PlayerNumber getFirstCompletion() {
        return firstCompletion;
    }

    public void setFirstCompletion(PlayerNumber firstCompletion) {
        this.firstCompletion = firstCompletion;
    }

    public int getStoneNumber() {
        return stoneNumber;
    }

    public void setStoneNumber(int stoneNumber) {
        this.stoneNumber = stoneNumber;
    }
}
