package com.markvdhorst.game.domain;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Card {
    public enum Color {
        BLUE,
        PURPLE,
        RED,
        YELLOW,
        GREEN,
        BROWN
    }

    private final Color color;
    private final int number;

    public Card(Color color, int number) {
        Objects.requireNonNull(color);
        if(number < 1 || number > 9) {
            throw new IllegalArgumentException("Invalid card number: " + number);
        }
        this.color = color;
        this.number = number;
    }

    public static Card of(Color color, int number) {
        return new Card(color, number);
    }

    public static @NotNull List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();

        for (int cardNumber = 1; cardNumber <= 9; cardNumber++) {
            for (Color color : Color.values()) {
                cards.add(new Card(color, cardNumber));
            }
        }
        return cards;
    }

    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }

    @SuppressWarnings("ObjectEquality")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return number == card.number &&
                color == card.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, number);
    }

    @Override
    public String toString() {
        return "Card{" +
                "color=" + color +
                ", number=" + number +
                '}';
    }
}
