package com.markvdhorst.game.domain.gamewincalculation;

import com.markvdhorst.game.domain.PlayerNumber;

class WinnerCounter {
    private PlayerNumber lastWinner;
    private int lastWinnerStonesInARow;

    void clearWinner() {
        lastWinner = null;
        lastWinnerStonesInARow = 0;
    }

    void setOrIncrementWinner(PlayerNumber playerNumber) {
        if(lastWinner == playerNumber) {
            lastWinnerStonesInARow ++;
        } else {
            lastWinner = playerNumber;
            lastWinnerStonesInARow = 1;
        }
    }

    boolean winnerHasThreeInARow() {
        return lastWinnerStonesInARow == 3;
    }

    PlayerNumber getLastWinner() {
        return lastWinner;
    }
}
