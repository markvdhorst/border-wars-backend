package com.markvdhorst.game.domain;

import java.util.ArrayList;
import java.util.List;

public class Hand {
    private List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public void removeCard(Card card) {
        if (!cards.contains(card)) {
            throw new IllegalArgumentException("Cannot play a card that is not in hand");
        }
        cards.remove(card);
    }

    public void addCard(Card card) {
        if (cards.size() == 6) {
            throw new IllegalStateException("Cannot add a card to a full hand");
        }

        cards.add(card);
    }

    public void reset() {
        cards.clear();
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Hand{" +
                "cards=" + cards +
                '}';
    }
}
