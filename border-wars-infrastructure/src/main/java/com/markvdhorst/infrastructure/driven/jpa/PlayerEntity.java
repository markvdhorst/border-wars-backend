package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.Card;
import com.markvdhorst.game.domain.Player;
import com.markvdhorst.game.domain.PlayerNumber;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "player")
public class PlayerEntity {

    @Id
    @GeneratedValue
    private long playerID;

    private UUID playerUUID;
    private String playerName;

    @Enumerated(EnumType.STRING)
    private PlayerNumber playerNumber;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "gameID")
    private GameEntity game;

    @OneToMany(mappedBy = "player", cascade = CascadeType.ALL)
    private List<CardEntity> cards = new ArrayList<>();

    public long getPlayerID() {
        return playerID;
    }

    public void setPlayerID(long playerID) {
        this.playerID = playerID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<CardEntity> getCards() {
        return cards;
    }

    public void setCards(List<CardEntity> cards) {
        this.cards = cards;
    }

    public void addCard(CardEntity cardEntity) {
        cards.add(cardEntity);
        cardEntity.setPlayer(this);
    }

    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(PlayerNumber playerNumber) {
        this.playerNumber = playerNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !PlayerEntity.class.isAssignableFrom(o.getClass())) {
            return false;
        }
        PlayerEntity that = (PlayerEntity) o;
        return playerUUID.equals(that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID);
    }

    public boolean isPlayerOne() {
        return playerNumber == PlayerNumber.ONE;
    }

    public boolean isPlayerTwo() {
        return playerNumber == PlayerNumber.TWO;
    }

    public Player toPlayer() {
        return new Player(playerNumber, playerName, playerUUID, mapCards());
    }

    @NotNull
    private List<Card> mapCards() {
        return cards.stream()
                .sorted()
                .map(CardEntity::toCard)
                .collect(Collectors.toList());
    }
}
