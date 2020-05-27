package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.PlayerNumber;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity(name = "game")
public class GameEntity {

    @Id
    @GeneratedValue()
    private long gameID;

    private UUID gameUUID;
    private PlayerNumber currentPlayer;
    private int currentTurn;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<PlayerEntity> players = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<CardEntity> cards = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<BorderStoneEntity> borderStones = new ArrayList<>();

    public long getGameID() {
        return gameID;
    }

    public void setGameID(long gameID) {
        this.gameID = gameID;
    }

    public List<PlayerEntity> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerEntity> players) {
        this.players = players;
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public void setGameUUID(UUID gameUUID) {
        this.gameUUID = gameUUID;
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }

    public PlayerNumber getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(PlayerNumber currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(int currentTurn) {
        this.currentTurn = currentTurn;
    }

    public List<BorderStoneEntity> getBorderStones() {
        return borderStones;
    }

    public void setBorderStones(List<BorderStoneEntity> borderStones) {
        this.borderStones = borderStones;
    }
    public void addBorderStone(BorderStoneEntity borderStoneEntity) {
        borderStones.add(borderStoneEntity);
        borderStoneEntity.setGame(this);
    }

    public void addPlayer(PlayerEntity player) {
        players.add(player);
        player.setGame(this);
    }
    public void addCard(CardEntity card) {
        cards.add(card);
        card.setGame(this);
    }
}
