package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.PlayerNumber;

import javax.persistence.*;
import java.util.List;

@Entity(name = "borderstone")
public class BorderStoneEntity {

    @Id
    @GeneratedValue()
    private long borderStoneID;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    private GameEntity game;

    private int stoneNumber;

    private PlayerNumber firstCompletion;

    @OneToMany(mappedBy = "borderStone", cascade = CascadeType.ALL)
    private List<PlayedCardEntity> playedCards;


    public long getBorderStoneID() {
        return borderStoneID;
    }

    public void setBorderStoneID(long borderStoneID) {
        this.borderStoneID = borderStoneID;
    }

    public int getStoneNumber() {
        return stoneNumber;
    }

    public void setStoneNumber(int stoneNumber) {
        this.stoneNumber = stoneNumber;
    }

    public PlayerNumber getFirstCompletion() {
        return firstCompletion;
    }

    public void setFirstCompletion(PlayerNumber firstCompletion) {
        this.firstCompletion = firstCompletion;
    }

    public List<PlayedCardEntity> getPlayedCards() {
        return playedCards;
    }

    public void setPlayedCards(List<PlayedCardEntity> playedCards) {
        this.playedCards = playedCards;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public void addPlayedCard(PlayerNumber playerNumber, CardEntity cardEntity) {
        PlayedCardEntity playedCardEntity = new PlayedCardEntity();
        playedCardEntity.setPlayerNumber(playerNumber);
        playedCardEntity.setCard(cardEntity);
        playedCardEntity.setBorderStone(this);
        playedCards.add(playedCardEntity);
    }
}
