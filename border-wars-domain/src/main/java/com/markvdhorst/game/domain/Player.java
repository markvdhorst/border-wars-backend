package com.markvdhorst.game.domain;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Player {
    private final String name;
    private final PlayerNumber number;
    private final PlayerUUID playerUuid;

    private final Hand hand;

    Player(PlayerNumber number, String name, PlayerUUID playerUuid) {
        this.number = Objects.requireNonNull(number);
        this.name = Objects.requireNonNull(name);
        this.playerUuid = playerUuid;
        hand = new Hand();
    }

    public Player(PlayerNumber number, String name, UUID playerUUID, List<Card> cardsInHand) {
        this.number = number;
        this.name = name;
        this.playerUuid = new PlayerUUID(playerUUID);
        this.hand = new Hand(cardsInHand);
    }

    public void emptyHand() {
        hand.reset();
    }

    void addCard(Card card) {
        hand.addCard(card);
    }

    public PlayerNumber getNumber() {
        return number;
    }

    public PlayerUUID getPlayerUuid() {
        return playerUuid;
    }

    void playCard(Board board, int borderStone, Card card) {
        hand.removeCard(card);
        board.playCard(borderStone, getNumber(), card);
    }

    public String getName() {
        return name;
    }

    public List<Card> getCardsInHand() {
        return hand.getCards();
    }

    @SuppressWarnings("ObjectEquality")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return number == player.number;
    }

    @Override
    public int hashCode() {
        return Objects.hash(number);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", number=" + number +
                ", playerUuid=" + playerUuid +
                ", hand=" + hand +
                '}';
    }
}
