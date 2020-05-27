package com.markvdhorst.game.domain.gamewincalculation;

import com.markvdhorst.game.domain.PlayerNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameWinCalculatorTest {

    private GameWinCalculator gameWinCalculator;

    @BeforeEach
    void setUp() {
        gameWinCalculator = new GameWinCalculator();
    }

    @Test
    void gameWithNoStoneWinnersShouldHaveNoWinners() {
        List<StoneWinState> stoneWinStateList = new ArrayList<>();
        for (int i = 1; i <= 9; i++) {
            stoneWinStateList.add(new StoneWinStateStub(i, null));
        }

        assertEquals(Optional.empty(), gameWinCalculator.determineWinner(stoneWinStateList));
    }

    @Test
    void playerWithThreeStonesInARowShouldWin() {
        List<StoneWinState> stoneWinStateList = List.of(
                new StoneWinStateStub(1, PlayerNumber.ONE),
                new StoneWinStateStub(2, PlayerNumber.ONE),
                new StoneWinStateStub(3, PlayerNumber.ONE),
                new StoneWinStateStub(4, null),
                new StoneWinStateStub(5, null),
                new StoneWinStateStub(6, null),
                new StoneWinStateStub(7, null),
                new StoneWinStateStub(8, null),
                new StoneWinStateStub(9, null)
        );


        assertEquals(Optional.of(PlayerNumber.ONE), gameWinCalculator.determineWinner(stoneWinStateList));
    }

    @Test
    void playerOneShouldWinWithThreeStonesInARow() {
        List<StoneWinState> stoneWinStateList = List.of(
                new StoneWinStateStub(1, PlayerNumber.ONE),
                new StoneWinStateStub(2, PlayerNumber.TWO),
                new StoneWinStateStub(3, PlayerNumber.ONE),
                new StoneWinStateStub(4, PlayerNumber.ONE),
                new StoneWinStateStub(5, PlayerNumber.ONE),
                new StoneWinStateStub(6, PlayerNumber.TWO),
                new StoneWinStateStub(7, PlayerNumber.TWO),
                new StoneWinStateStub(8, null),
                new StoneWinStateStub(9, null)
        );

        assertEquals(Optional.of(PlayerNumber.ONE), gameWinCalculator.determineWinner(stoneWinStateList));
    }

    @Test
    void playerTwoShouldWinWith5StonesTo4() {
        List<StoneWinState> stoneWinStateList = List.of(
                new StoneWinStateStub(1, PlayerNumber.ONE),
                new StoneWinStateStub(2, PlayerNumber.TWO),
                new StoneWinStateStub(3, PlayerNumber.ONE),
                new StoneWinStateStub(4, PlayerNumber.TWO),
                new StoneWinStateStub(5, PlayerNumber.ONE),
                new StoneWinStateStub(6, PlayerNumber.TWO),
                new StoneWinStateStub(7, PlayerNumber.TWO),
                new StoneWinStateStub(8, PlayerNumber.ONE),
                new StoneWinStateStub(9, PlayerNumber.TWO)
        );

        assertEquals(Optional.of(PlayerNumber.TWO), gameWinCalculator.determineWinner(stoneWinStateList));
    }

   @Test
    void playerOneShouldWinWith5Stones() {
        List<StoneWinState> stoneWinStateList = List.of(
                new StoneWinStateStub(1, PlayerNumber.ONE),
                new StoneWinStateStub(2, PlayerNumber.ONE),
                new StoneWinStateStub(3, null),
                new StoneWinStateStub(4, PlayerNumber.ONE),
                new StoneWinStateStub(5, PlayerNumber.ONE),
                new StoneWinStateStub(6, null),
                new StoneWinStateStub(7, PlayerNumber.ONE),
                new StoneWinStateStub(8, null),
                new StoneWinStateStub(9, null)
        );

        assertEquals(Optional.of(PlayerNumber.ONE), gameWinCalculator.determineWinner(stoneWinStateList));
    }

   @Test
    void noWinnerYetWithFourStonesEach() {
        List<StoneWinState> stoneWinStateList = List.of(
                new StoneWinStateStub(1, PlayerNumber.ONE),
                new StoneWinStateStub(2, PlayerNumber.ONE),
                new StoneWinStateStub(3, PlayerNumber.TWO),
                new StoneWinStateStub(4, PlayerNumber.TWO),
                new StoneWinStateStub(5, PlayerNumber.ONE),
                new StoneWinStateStub(6, PlayerNumber.ONE),
                new StoneWinStateStub(7, PlayerNumber.TWO),
                new StoneWinStateStub(8, PlayerNumber.TWO),
                new StoneWinStateStub(9, null)
        );

        assertEquals(Optional.empty(), gameWinCalculator.determineWinner(stoneWinStateList));
    }

    private static class StoneWinStateStub implements StoneWinState {

        private final int stoneNumber;
        private final PlayerNumber winner;

        StoneWinStateStub(int stoneNumber, PlayerNumber winner) {
            this.stoneNumber = stoneNumber;
            this.winner = winner;
        }

        @Override
        public int getStoneNumber() {
            return stoneNumber;
        }

        @Override
        public Optional<PlayerNumber> getWinner() {
            return Optional.ofNullable(winner);
        }
    }
}
