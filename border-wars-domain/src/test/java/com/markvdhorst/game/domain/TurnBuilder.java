package com.markvdhorst.game.domain;

import java.util.ArrayList;
import java.util.List;

public class TurnBuilder {

    private List<StoneWin> resultingStoneWins = new ArrayList<>();
    private GameWin resultingGameWin;
    private Move move;

    public TurnBuilder(Move move) {
        this.move = move;
    }

    public TurnBuilder addExpectedStoneWin(StoneWin stoneWin) {
        resultingStoneWins.add(stoneWin);
        return this;
    }

    public TurnBuilder addExpectedGameWin(GameWin gameWin) {
        this.resultingGameWin = gameWin;
        return this;
    }

    public Turn build() {
        if (move == null) {
            throw new IllegalStateException("Cannot build turn without move");
        }
        return new Turn(move, resultingStoneWins, resultingGameWin);
    }
}
