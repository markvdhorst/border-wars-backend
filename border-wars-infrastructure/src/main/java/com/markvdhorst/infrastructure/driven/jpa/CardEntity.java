package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.Card;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity(name = "card")
public class CardEntity implements Comparable<CardEntity>{

    @Id
    @GeneratedValue()
    private long cardID;

    @Enumerated(EnumType.STRING)
    private Card.Color color;
    private int number;

    private int position;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "gameID")
    private GameEntity game;

    @ManyToOne(cascade = CascadeType.ALL)
    private PlayerEntity player;

    @OneToOne(mappedBy = "card", cascade = CascadeType.ALL)
    private PlayedCardEntity playedCard;

    public long getCardID() {
        return cardID;
    }

    public void setCardID(long cardID) {
        this.cardID = cardID;
    }

    public Card.Color getColor() {
        return color;
    }

    public void setColor(Card.Color color) {
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public GameEntity getGame() {
        return game;
    }

    public void setGame(GameEntity game) {
        this.game = game;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public void setPlayer(PlayerEntity player) {
        this.player = player;
    }

    public PlayedCardEntity getPlayedCard() {
        return playedCard;
    }

    public void setPlayedCard(PlayedCardEntity playedCard) {
        this.playedCard = playedCard;
        setPlayer(null);
    }

    public boolean equalValue(Card card) {
        return getNumber() == card.getNumber() && getColor() == card.getColor();
    }

    public Card toCard() {
        return new Card(color, number);
    }

    public boolean isInDeck() {
        return player == null && playedCard == null;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public int compareTo(@NotNull CardEntity o) {
        return Integer.compare(position, o.position);
    }
}
