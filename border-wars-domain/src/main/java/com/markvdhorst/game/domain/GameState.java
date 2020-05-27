package com.markvdhorst.game.domain;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GameState {

    private final GameStatus gameStatus;
    private final PlayerNumber currentPlayer;
    private final List<Card> myHand;
    private final PlayerNumber winner;
    private final List<StoneState> borderStones;
    private final PlayerNumber yourPlayerNumber;
    private final Map<PlayerNumber, String> playerNames;
    private final int currentTurn;

    GameState(GameStatus gameStatus, PlayerNumber currentPlayer, List<Card> myHand, PlayerNumber winner, List<StoneState> borderStones, PlayerNumber yourPlayerNumber, Map<PlayerNumber, String> playerNames, int currentTurn) {
        this.gameStatus = gameStatus;
        this.currentPlayer = currentPlayer;
        this.myHand = myHand;
        this.winner = winner;
        this.borderStones = borderStones;
        this.yourPlayerNumber = yourPlayerNumber;
        this.playerNames = playerNames;
        this.currentTurn = currentTurn;
    }

    public PlayerNumber getCurrentPlayer() {
        return currentPlayer;
    }

    public List<Card> getMyHand() {
        return myHand;
    }

    public Optional<PlayerNumber> getWinner() {
        return Optional.ofNullable(winner);
    }

    public List<StoneState> getBorderStones() {
        return borderStones;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public PlayerNumber getYourPlayerNumber() {
        return yourPlayerNumber;
    }

    public Map<PlayerNumber, String> getPlayerNames() {
        return playerNames;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }
}
