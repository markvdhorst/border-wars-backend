package com.markvdhorst.game.api;

import com.markvdhorst.game.domain.PlayerUUID;
import com.markvdhorst.game.domain.GameState;

public interface GameService {

    /**
     * Register(With name), Get PlayerUUID(Which identifies your player in a certain game)
     * @param playerName
     * @return
     */
    PlayerUUID register(String playerName);

    /**
     * Get Game state (Which could also be not finished) Waiting/Playing/Finished
     * @param player uuid received from the register action
     * @return
     */
    GameState getGameState(PlayerUUID player);

    /**
     * Play a card
     * @param playerUUID
     * @param playCardCommand
     */
    void playCard(PlayerUUID playerUUID, PlayCardCommand playCardCommand);
}
