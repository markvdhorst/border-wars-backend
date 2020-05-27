package com.markvdhorst.game.api;

public class PlayCardCommand {
    private CardDTO card;
    private int borderStone;

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public void setBorderStone(int borderStone) {
        this.borderStone = borderStone;
    }

    public CardDTO getCard() {
        return card;
    }

    public int getBorderStone() {
        return borderStone;
    }
}
