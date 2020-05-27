package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.PlayerNumber;

import javax.persistence.*;

@Entity(name = "playedcard")
public class PlayedCardEntity {

    @Id
    @GeneratedValue
    private long playedCardID;

    // Save only the playedNumber because that's what the Game object needs
    private PlayerNumber playerNumber;

    @OneToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "cardID")
    private CardEntity card;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "borderStoneID")
    private BorderStoneEntity borderStone;

    private int position;

    public long getPlayedCardID() {
        return playedCardID;
    }

    public void setPlayedCardID(long playedCardID) {
        this.playedCardID = playedCardID;
    }

    public PlayerNumber getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(PlayerNumber playerNumber) {
        this.playerNumber = playerNumber;
    }

    public CardEntity getCard() {
        return card;
    }

    public void setCard(CardEntity card) {
        this.card = card;
        card.setPlayedCard(this);
    }

    public BorderStoneEntity getBorderStone() {
        return borderStone;
    }

    public void setBorderStone(BorderStoneEntity borderStone) {
        this.borderStone = borderStone;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
