package com.markvdhorst.game.domain;

import java.util.*;

public class Deck {
    private final Deque<Card> cards;

    Deck(List<Card> cards) {
        Objects.requireNonNull(cards);
        validateCardUniqueness(cards);
        this.cards = new ArrayDeque<>(cards);
    }

    private void validateCardUniqueness(List<Card> cards) {
        Set<Card> foundCards = new HashSet<>();
        for (Card card : cards) {
            if(foundCards.contains(card)) {
                throw new IllegalArgumentException("Deck contains non-unique card: " + card);
            }
            foundCards.add(card);
        }
    }


    public Optional<Card> takeNextCard() {
        return Optional.ofNullable(cards.pollFirst());
    }

    public static Deck shuffledDeck() {
        List<Card> cards = Card.getAllCards();
        Collections.shuffle(cards);
        return new Deck(cards);
    }

    public Collection<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "cards=" + cards +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Deck deck = (Deck) o;
        return Objects.equals(cards, deck.cards);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cards);
    }
}
