package com.markvdhorst.game.domain.gamewincalculation;

import com.markvdhorst.game.domain.PlayerNumber;

import java.util.Optional;

public interface StoneWinState {
    int getStoneNumber();
    Optional<PlayerNumber> getWinner();
}
