package com.markvdhorst.game.domain;

import com.markvdhorst.game.api.*;
import com.markvdhorst.game.ports.GameRepository;

import java.util.Optional;
import java.util.UUID;

public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public PlayerUUID register(String playerName) {
        PlayerUUID playerUUID = new PlayerUUID(UUID.randomUUID());

        Optional<Game> existingGame = gameRepository.getGameWaitingForPlayer();
        if (existingGame.isPresent()) {
            addPlayerAndStartGame(existingGame.get(), playerName, playerUUID);
        } else {
            createGame(playerName, playerUUID);
        }
        return playerUUID;
    }

    private void createGame(String playerName, PlayerUUID playerUUID) {
        Game game = new Game(playerName, playerUUID);
        gameRepository.create(game);
    }

    private void addPlayerAndStartGame(Game game, String playerName, PlayerUUID playerUUID) {
        game.addSecondPlayer(playerName, playerUUID);
        game.startGame();
        gameRepository.save(game);
    }

    @Override
    public GameState getGameState(PlayerUUID player) {
        Game game = gameRepository.findGameByPlayerUUID(player);
        return game.getState(player);
    }

    @Override
    public void playCard(PlayerUUID playerUUID, PlayCardCommand playCardCommand) {
        Game game = gameRepository.findGameByPlayerUUID(playerUUID);
        game.playCard(playerUUID, playCardCommand.getCard().toCard(), playCardCommand.getBorderStone());
        gameRepository.save(game);
    }
}
