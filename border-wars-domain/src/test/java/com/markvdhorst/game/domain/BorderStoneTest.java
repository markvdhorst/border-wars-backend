package com.markvdhorst.game.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.markvdhorst.game.domain.Card.Color.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class BorderStoneTest {

    private BorderStone borderStone;

    @BeforeEach
    void setUp() {
        borderStone = new BorderStone(1);
    }

    @Test
    void highestNumberColoredRowWins() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 3),
                Card.of(BLUE, 4),
                Card.of(BLUE, 5));

        playCards(PlayerNumber.TWO,
                Card.of(YELLOW, 4),
                Card.of(YELLOW, 5),
                Card.of(YELLOW, 6));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.TWO, actual);
    }

    @Test
    void coloredRowIsHigherThanThreeTheSame() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 3),
                Card.of(BLUE, 4),
                Card.of(BLUE, 5));

        playCards(PlayerNumber.TWO,
                Card.of(YELLOW, 9),
                Card.of(BLUE, 9),
                Card.of(BROWN, 9));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.ONE, actual);
    }

    @Test
    void threeTheSameIsHigherThanSameColor() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 9),
                Card.of(BLUE, 7),
                Card.of(BLUE, 6));

        playCards(PlayerNumber.TWO,
                Card.of(YELLOW, 3),
                Card.of(BLUE, 3),
                Card.of(BROWN, 3));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.TWO, actual);
    }

    @Test
    void sameColorIsHigherThanThreeInARow() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 9),
                Card.of(BLUE, 7),
                Card.of(BLUE, 6));

        playCards(PlayerNumber.TWO,
                Card.of(YELLOW, 3),
                Card.of(BLUE, 4),
                Card.of(BROWN, 5));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.ONE, actual);
    }

    @Test
    void threeInARowIsHigherThanNoSpecialCombo() {
        playCards(PlayerNumber.ONE,
                Card.of(YELLOW, 9),
                Card.of(BLUE, 7),
                Card.of(BLUE, 6));

        playCards(PlayerNumber.TWO,
                Card.of(YELLOW, 3),
                Card.of(BLUE, 4),
                Card.of(BROWN, 5));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.TWO, actual);
    }

    @Test
    void sameColorSevenEightNineShouldAlwaysWinBorderStone() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 7),
                Card.of(BLUE, 8),
                Card.of(BLUE, 9));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.ONE, actual);
    }

    @Test
    void sameColorRowShouldWinAgainstIncompleteThreeTheSame() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 1),
                Card.of(BLUE, 2),
                Card.of(BLUE, 3));

        playCards(PlayerNumber.TWO,
                Card.of(BLUE, 9),
                Card.of(BROWN, 9)
        );

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.ONE, actual);
    }

    @Test
    void lowColorRowShouldNotWinIfHigherColoredRowIsPossible() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 1),
                Card.of(BLUE, 2),
                Card.of(BLUE, 3));


        Optional<PlayerNumber> actual = borderStone.getWinningPlayer(Collections.emptyList());
        assertEquals(Optional.empty(), actual);
    }

    @Test
    void lowThreeInARowShouldWinAgainstHigherIfHigherCannotBeFinished() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 9),
                Card.of(YELLOW, 9));

        playCards(PlayerNumber.TWO,
                Card.of(BLUE, 3),
                Card.of(YELLOW, 3),
                Card.of(BROWN, 3));

        List<Card> visibleCards = List.of(
                Card.of(BROWN, 9),
                Card.of(RED, 9),
                Card.of(GREEN, 9),
                Card.of(PURPLE, 9));

        PlayerNumber actual = borderStone.getWinningPlayer(visibleCards).orElse(null);
        assertEquals(PlayerNumber.TWO, actual);
    }

    @Test
    void lowThreeInARowShouldNotWinIfHigherIsPossible() {
        playCards(PlayerNumber.ONE,
                Card.of(BLUE, 9),
                Card.of(YELLOW, 9));

        playCards(PlayerNumber.TWO,
                Card.of(BLUE, 3),
                Card.of(YELLOW, 3),
                Card.of(BROWN, 3));

        PlayerNumber actual = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertNull(actual);
    }

    @Test
    void winnerShouldNotChangeEvenWhenOtherPlayerFinishesRow() {
        playCards(PlayerNumber.TWO,
                Card.of(BLUE, 7),
                Card.of(BLUE, 8),
                Card.of(BLUE, 9));

        PlayerNumber actualAfterThreeCards = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.TWO, actualAfterThreeCards);

        playCards(PlayerNumber.ONE,
                Card.of(RED, 7),
                Card.of(RED, 8),
                Card.of(RED, 9));

        PlayerNumber winningPlayerAfterStoneIsComplete = borderStone.getWinningPlayer(Collections.emptyList()).orElse(null);
        assertEquals(PlayerNumber.TWO, winningPlayerAfterStoneIsComplete);
    }

    private void playCards(PlayerNumber playerNumber, Card... cards) {
        for (Card card : cards) {
            borderStone.playCard(playerNumber, card);
        }
    }
}
