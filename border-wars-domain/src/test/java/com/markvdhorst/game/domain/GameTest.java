package com.markvdhorst.game.domain;

import org.junit.jupiter.api.Test;

import java.util.*;

import static com.markvdhorst.game.domain.Card.Color.*;
import static com.markvdhorst.game.domain.Card.of;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private static final String FIRST_PLAYER_NAME = "Mark";
    private static final String SECOND_PLAYER_NAME = "Britta";
    private static final PlayerUUID FIRST_PLAYER_UUID = new PlayerUUID(UUID.randomUUID());
    private static final PlayerUUID SECOND_PLAYER_UUID = new PlayerUUID(UUID.randomUUID());

    @Test
    void testFirstEndToEndGame() {
        Game game = new Game(this::getFirstEndToEndDeck, FIRST_PLAYER_NAME, FIRST_PLAYER_UUID);
        game.addSecondPlayer(SECOND_PLAYER_NAME, SECOND_PLAYER_UUID);
        game.startGame();

        GameState stateForPlayerOne = game.getState(FIRST_PLAYER_UUID);
        Map<PlayerNumber, String> playerNames = stateForPlayerOne.getPlayerNames();
        assertEquals(FIRST_PLAYER_NAME, playerNames.get(stateForPlayerOne.getCurrentPlayer()));
        assertIterableEquals(getFirstPlayerHandAfterFirstTurn(), stateForPlayerOne.getMyHand());

        GameState stateForPlayerTwo = game.getState(SECOND_PLAYER_UUID);
        assertEquals(FIRST_PLAYER_NAME, playerNames.get(stateForPlayerTwo.getCurrentPlayer()));
        assertIterableEquals(getSecondPlayerHandAfterFirstTurn(), stateForPlayerTwo.getMyHand());

        GameFileParser gameFileParser = new GameFileParser("testgame1");

        Map<Integer, PlayerNumber> expectedStoneWins = new HashMap<>();
        String expectedWinnerName = null;

        for (Turn turn : gameFileParser.getTurns()) {
            game.playCard(getPlayerUUID(turn.getPlayer()), turn.getPlayedCard(), turn.getPlayedOnStone());
            GameState newState = game.getState(getPlayerUUID(turn.getPlayer()));

            // Add expected stone wins
            for (StoneWin resultingStoneWin : turn.getResultingStoneWins()) {
                expectedStoneWins.put(resultingStoneWin.getStone(), resultingStoneWin.getPlayer());
            }

            if (turn.getResultingGameWin().isPresent()) {
                expectedWinnerName = getExpectedWinnerName(turn.getResultingGameWin().get());
            }

            for (StoneState stone : newState.getBorderStones()) {
                assertStoneWinner(stone, expectedStoneWins);
            }

            assertGameWinner(newState, expectedWinnerName);
        }
    }

    private PlayerUUID getPlayerUUID(PlayerNumber playerNumber) {
        switch (playerNumber) {
            case ONE:
                return FIRST_PLAYER_UUID;
            case TWO:
                return SECOND_PLAYER_UUID;
            default:
                return fail("Unexpected player number " + playerNumber);
        }
    }

    private void assertStoneWinner(StoneState stone, Map<Integer, PlayerNumber> expectedStoneWins) {
        if (expectedStoneWins.containsKey(stone.getStoneNumber())) {
            assertEquals(expectedStoneWins.get(stone.getStoneNumber()), stone.getWinner().orElse(null));
        } else {
            assertEquals(Optional.empty(), stone.getWinner());
        }
    }

    private void assertGameWinner(GameState newState, String expectedWinnerName) {
        if (expectedWinnerName == null) {
            assertTrue(newState.getWinner().isEmpty());
        } else {
            assertEquals(expectedWinnerName, newState.getWinner().map(newState.getPlayerNames()::get).orElse(null));
        }
    }

    private String getExpectedWinnerName(GameWin gameWin) {
        switch (gameWin.getPlayer()) {
            case ONE:
                return FIRST_PLAYER_NAME;
            case TWO:
                return SECOND_PLAYER_NAME;
            default:
                return fail();
        }
    }

    private List<Card> getFirstPlayerHandAfterFirstTurn() {
        return List.of(
                of(RED, 5),
                of(BLUE, 4),
                of(RED, 6),
                of(GREEN, 9),
                of(YELLOW, 6),
                of(BLUE, 2)
        );
    }

    private List<Card> getSecondPlayerHandAfterFirstTurn() {
        return List.of(
                of(BROWN, 4),
                of(PURPLE, 8),
                of(BROWN, 6),
                of(PURPLE, 9),
                of(BROWN, 5),
                of(YELLOW, 4)
        );
    }

    private Deck getFirstEndToEndDeck() {
        List<Card> cards = List.of(
                of(RED, 5),
                of(BROWN, 4),
                of(BLUE, 4),
                of(PURPLE, 8),
                of(RED, 6),
                of(BROWN, 6),
                of(GREEN, 9),
                of(PURPLE, 9),
                of(YELLOW, 6),
                of(BROWN, 5),
                of(BLUE, 2),
                of(YELLOW, 4),
                of(RED, 2),
                of(RED, 9),
                of(BLUE, 3),
                of(BLUE, 8),
                of(YELLOW, 7),
                of(BROWN, 8),
                of(PURPLE, 2),
                of(YELLOW, 5),
                of(YELLOW, 3),
                of(BROWN, 1),
                of(PURPLE, 5),
                of(BROWN, 9),
                of(RED, 7),
                of(BLUE, 6),
                of(BLUE, 5),
                of(BLUE, 7),
                of(YELLOW, 8),
                of(BROWN, 3),
                of(GREEN, 1),
                of(RED, 4),
                of(PURPLE, 3),
                of(YELLOW, 1),
                of(YELLOW, 2),
                of(GREEN, 5),
                of(GREEN, 8),
                of(RED, 1),
                of(YELLOW, 9),
                of(PURPLE, 1),
                of(GREEN, 7),
                of(BLUE, 1),
                of(GREEN, 6),
                of(PURPLE, 7),
                of(BLUE, 9),
                of(BROWN, 2),
                of(GREEN, 3),
                of(PURPLE, 4),
                of(RED, 8),
                of(RED, 3),
                of(GREEN, 4),
                of(GREEN, 2),
                of(BROWN, 7),
                of(PURPLE, 6)
        );

        assertEquals(54, cards.size());
        return new Deck(cards);
    }


}
