package com.markvdhorst.game.ports;

import com.markvdhorst.game.domain.PlayerUUID;
import com.markvdhorst.game.domain.Game;

import java.util.Optional;

public interface GameRepository {
    Optional<Game> getGameWaitingForPlayer();

    void save(Game game);

    void create(Game game);

    Game findGameByPlayerUUID(PlayerUUID playerUUID);
}
