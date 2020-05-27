package com.markvdhorst.game.domain.gamewincalculation;

import com.markvdhorst.game.domain.BorderStone;
import com.markvdhorst.game.domain.Card;
import com.markvdhorst.game.domain.PlayerNumber;

import java.util.List;
import java.util.Optional;

public class BorderStoneWinStateAdapter implements StoneWinState {

    private final BorderStone borderStone;
    private final List<Card> visibleCards;

    public BorderStoneWinStateAdapter(BorderStone borderStone, List<Card> visibleCards) {
        this.borderStone = borderStone;
        this.visibleCards = visibleCards;
    }

    @Override
    public int getStoneNumber() {
        return borderStone.getStoneNumber();
    }

    @Override
    public Optional<PlayerNumber> getWinner() {
        return borderStone.getWinningPlayer(visibleCards);
    }
}
