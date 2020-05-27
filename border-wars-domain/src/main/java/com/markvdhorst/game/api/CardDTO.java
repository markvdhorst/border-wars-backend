package com.markvdhorst.game.api;

import com.markvdhorst.game.domain.Card;

public class CardDTO {
    public enum Color {
        BLUE,
        PURPLE,
        RED,
        YELLOW,
        GREEN,
        BROWN
    }

    private Color color;
    private int number;

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Card toCard() {
        return new Card(Card.Color.valueOf(color.name()), number);
    }
}
