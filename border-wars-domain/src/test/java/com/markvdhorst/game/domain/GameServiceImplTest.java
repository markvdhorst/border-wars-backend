package com.markvdhorst.game.domain;

import com.markvdhorst.game.api.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.markvdhorst.game.domain.GameStatus.PLAYING;
import static com.markvdhorst.game.domain.GameStatus.WAITING_FOR_SECOND_PLAYER;
import static org.junit.jupiter.api.Assertions.assertEquals;

class GameServiceImplTest {

    private static final String PLAYER_ONE_NAME = "Mark";
    private static final String PLAYER_TWO_NAME = "Britta";
    private GameService gameService;

    @BeforeEach
    void setUp() {
        gameService = new GameServiceImpl(new TestGameRepository());
    }

    @Test
    void testTwoRegisteringPlayersShouldStartTheGameTogether() {
        PlayerUUID playerOneHandle = gameService.register(PLAYER_ONE_NAME);
        assertEquals(WAITING_FOR_SECOND_PLAYER, gameService.getGameState(playerOneHandle).getGameStatus());
        PlayerUUID secondPlayerHandle = gameService.register(PLAYER_TWO_NAME);
        assertEquals(PLAYING, gameService.getGameState(playerOneHandle).getGameStatus());
        assertEquals(PLAYING, gameService.getGameState(secondPlayerHandle).getGameStatus());
    }

    @Test
    void cardShouldGetDealedAfterSecondPlayerRegisters() {
        PlayerUUID playerOneHandle = gameService.register(PLAYER_ONE_NAME);
        PlayerUUID secondPlayerHandle = gameService.register(PLAYER_TWO_NAME);

        GameState gameState = gameService.getGameState(playerOneHandle);

        assertEquals(PLAYING, gameState.getGameStatus());
        assertEquals(6, gameState.getMyHand().size());
    }

    @Test
    void playerNumberShouldBeReturnedWithState() {
        PlayerUUID playerOneHandle = gameService.register(PLAYER_ONE_NAME);
        PlayerUUID secondPlayerHandle = gameService.register(PLAYER_TWO_NAME);

        assertEquals(PlayerNumber.ONE, gameService.getGameState(playerOneHandle).getYourPlayerNumber());
        assertEquals(PlayerNumber.TWO, gameService.getGameState(secondPlayerHandle).getYourPlayerNumber());
    }

    @Test
    void playerNamesShouldBeReturned() {
        PlayerUUID playerOneHandle = gameService.register(PLAYER_ONE_NAME);
        PlayerUUID secondPlayerHandle = gameService.register(PLAYER_TWO_NAME);

        Map<PlayerNumber, String> playerNames = gameService.getGameState(playerOneHandle).getPlayerNames();
        assertEquals(PLAYER_ONE_NAME, playerNames.get(PlayerNumber.ONE));
        assertEquals(PLAYER_TWO_NAME, playerNames.get(PlayerNumber.TWO));
    }
}
