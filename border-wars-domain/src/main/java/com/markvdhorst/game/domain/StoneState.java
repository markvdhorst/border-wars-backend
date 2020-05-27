package com.markvdhorst.game.domain;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class StoneState {

    private final int stoneNumber;
    private final PlayerNumber winner;
    private final Map<PlayerNumber, List<Card>> playedCards;

    StoneState(int stoneNumber, @Nullable PlayerNumber winner, Map<PlayerNumber, List<Card>> playedCards) {
        this.stoneNumber = stoneNumber;
        this.winner = winner;
        this.playedCards = playedCards;
    }

    public int getStoneNumber() {
        return stoneNumber;
    }

    public Optional<PlayerNumber> getWinner() {
        return Optional.ofNullable(winner);
    }

    public Map<PlayerNumber, List<Card>> getPlayedCards() {
        return playedCards;
    }
}
