package com.markvdhorst.infrastructure.driven;

import com.markvdhorst.game.domain.Game;
import com.markvdhorst.game.domain.PlayerUUID;
import com.markvdhorst.game.ports.GameRepository;

import java.util.Optional;

/**
 * Writes the game to the backup repository on Create/Save.
 * Checks validity on read operations.
 */
public class BackupWriteReadGameRepository implements GameRepository {

    private final GameRepository mainRepository;
    private final GameRepository backupRepository;

    public BackupWriteReadGameRepository(GameRepository mainRepository, GameRepository backupRepository) {
        this.mainRepository = mainRepository;
        this.backupRepository = backupRepository;
    }

    @Override
    public Optional<Game> getGameWaitingForPlayer() {
        Optional<Game> gameWaitingForPlayer = mainRepository.getGameWaitingForPlayer();
        Optional<Game> backupGameWaitingForPlayer = backupRepository.getGameWaitingForPlayer();

        if(backupGameWaitingForPlayer.equals(gameWaitingForPlayer)) {
            System.out.println("getGameWaitingForPlayer: Retrieved games are equal");
        } else {
            System.out.println(String.format("getGameWaitingForPlayer: retrieved games are not equal \ngameWaitingForPlayer       = %s\nbackupGameWaitingForPlayer = %s", gameWaitingForPlayer, backupGameWaitingForPlayer));
        }

        return gameWaitingForPlayer;
    }

    @Override
    public void save(Game game) {
        mainRepository.save(game);
        backupRepository.save(game);
    }

    @Override
    public void create(Game game) {
        mainRepository.create(game);
        backupRepository.create(game);
    }

    @Override
    public Game findGameByPlayerUUID(PlayerUUID playerUUID) {
        Game gameByPlayerUUID = mainRepository.findGameByPlayerUUID(playerUUID);
        Game backupGameByPlayerUUID = backupRepository.findGameByPlayerUUID(playerUUID);

        if(gameByPlayerUUID.equals(backupGameByPlayerUUID)) {
            System.out.println("findGameByPlayerUUID: Retrieved games are equal");
        } else {
            System.out.println(String.format("findGameByPlayerUUID: Retrieved games are not equal \ngameWaitingForPlayer       = %s\nbackupGameWaitingForPlayer = %s", gameByPlayerUUID, backupGameByPlayerUUID));
        }

        return gameByPlayerUUID;
    }
}
