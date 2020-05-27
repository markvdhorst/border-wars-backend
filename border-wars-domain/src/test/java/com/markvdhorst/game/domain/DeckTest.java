package com.markvdhorst.game.domain;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void deckReturnsCardFromInput() {
        Card card = new Card(Card.Color.BLUE, 9);
        Deck deck = new Deck(List.of(card));
        assertEquals(card, deck.takeNextCard().orElseGet(() -> fail("Expected a card to be present")));
    }

    @Test
    void deckReturnsFirstCardFirst() {
        Card firstCard = new Card(Card.Color.BLUE, 9);
        Card secondCard = new Card(Card.Color.BROWN, 7);
        Deck deck = new Deck(List.of(firstCard, secondCard));
        assertEquals(firstCard, deck.takeNextCard().orElseGet(() -> fail("Expected a card to be present")));
        assertEquals(secondCard, deck.takeNextCard().orElseGet(() -> fail("Expected a card to be present")));
    }

    @Test
    void emptyDeckReturnsEmptyOptional() {
        Deck deck = new Deck(Collections.emptyList());
        assertTrue(deck.takeNextCard().isEmpty());
    }
}