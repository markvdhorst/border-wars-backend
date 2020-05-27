package com.markvdhorst.game.domain;

import java.util.Objects;
import java.util.UUID;

public class PlayerUUID {
    private final UUID playerUUID;

    public PlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String stringValue() {
        return playerUUID.toString();
    }

    public static PlayerUUID fromString(String value) {
        return new PlayerUUID(UUID.fromString(value));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerUUID that = (PlayerUUID) o;
        return playerUUID.equals(that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID);
    }

    @Override
    public String toString() {
        return "PlayerUUID{" +
                "playerUUID=" + playerUUID +
                '}';
    }
}
