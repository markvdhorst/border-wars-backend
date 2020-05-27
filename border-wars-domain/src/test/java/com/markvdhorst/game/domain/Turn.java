package com.markvdhorst.game.domain;

import java.util.List;
import java.util.Optional;

public class Turn {

    private final List<StoneWin> resultingStoneWins;
    private final GameWin resultingGameWin;
    private final Move move;

    public Turn(Move move, List<StoneWin> resultingStoneWins, GameWin resultingGameWin) {
        this.move = move;
        this.resultingStoneWins = resultingStoneWins;
        this.resultingGameWin = resultingGameWin;
    }

    public Move getMove() {
        return move;
    }

    public List<StoneWin> getResultingStoneWins() {
        return resultingStoneWins;
    }

    public Optional<GameWin> getResultingGameWin() {
        return Optional.ofNullable(resultingGameWin);
    }

    int getPlayedOnStone() {
        return getMove().getStone();
    }

    Card getPlayedCard() {
        return getMove().getPlayedCard();
    }

    PlayerNumber getPlayer() {
        return getMove().getPlayer();
    }

    @Override
    public String toString() {
        return "Turn{" +
                "resultingStoneWins=" + resultingStoneWins +
                ", resultingGameWin=" + resultingGameWin +
                ", move=" + move +
                '}';
    }
}
