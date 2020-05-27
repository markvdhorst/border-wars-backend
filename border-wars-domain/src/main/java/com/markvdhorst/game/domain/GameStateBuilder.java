package com.markvdhorst.game.domain;

import java.util.List;
import java.util.Map;

public class GameStateBuilder {
    private GameStatus gameStatus;
    private PlayerNumber currentPlayerDTO;
    private List<Card> myHand;
    private PlayerNumber winner;
    private List<StoneState> stones;
    private PlayerNumber yourPlayerNumber;
    private Map<PlayerNumber, String> playerNames;
    private int currentTurn;

    public GameStateBuilder withGameStatus(GameStatus gameStatus) {
        this.gameStatus = gameStatus;
        return this;
    }

    public GameStateBuilder withCurrentPlayer(PlayerNumber currentPlayerDTO) {
        this.currentPlayerDTO = currentPlayerDTO;
        return this;
    }

    public GameStateBuilder withMyHand(List<Card> myHand) {
        this.myHand = myHand;
        return this;
    }

    public GameStateBuilder withWinner(PlayerNumber winner) {
        this.winner = winner;
        return this;
    }

    public GameStateBuilder withStones(List<StoneState> stones) {
        this.stones = stones;
        return this;
    }

    public GameStateBuilder withYourPlayerNumber(PlayerNumber yourPlayerNumber) {
        this.yourPlayerNumber = yourPlayerNumber;
        return this;
    }

    public GameStateBuilder withPlayerNames(Map<PlayerNumber, String> playerNames) {
        this.playerNames = playerNames;
        return this;
    }

    public GameState createGameState() {
        return new GameState(gameStatus, currentPlayerDTO, myHand, winner, stones, yourPlayerNumber, playerNames, currentTurn);
    }

    public GameStateBuilder withTurn(int currentTurn) {
        this.currentTurn = currentTurn;
        return this;
    }
}
