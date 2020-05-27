package com.markvdhorst.game.domain;

import com.markvdhorst.game.ports.GameRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class TestGameRepository implements GameRepository {

    private final List<Game> games = new ArrayList<>();

    @Override
    public Optional<Game> getGameWaitingForPlayer() {
        return games.stream().
                filter(game -> game.getStatus() == GameStatus.WAITING_FOR_SECOND_PLAYER)
                .findFirst();
    }

    @Override
    public void save(Game game) {
        // No op for this repo
    }

    @Override
    public void create(Game game) {
        games.add(game);
    }

    @Override
    public Game findGameByPlayerUUID(PlayerUUID playerUUID) {
        return games.stream().filter(containsPlayer(playerUUID)).findFirst().orElseThrow();
    }

    private Predicate<Game> containsPlayer(PlayerUUID playerUUID) {
        return game -> game.getPlayers().stream().
                map(Player::getPlayerUuid)
                .anyMatch(playerUUID::equals);
    }

}
