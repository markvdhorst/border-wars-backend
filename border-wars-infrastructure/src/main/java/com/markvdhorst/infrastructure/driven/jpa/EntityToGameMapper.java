package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.*;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class EntityToGameMapper {

    public Game toGame(GameEntity gameEntity) {
        Player firstPlayer = getPlayer(gameEntity, PlayerEntity::isPlayerOne);
        Player secondPlayer = getPlayer(gameEntity, PlayerEntity::isPlayerTwo);
        List<Card> cardsInDeck = secondPlayer == null ? null : getCardsInDeck(gameEntity);
        Player currentPlayer = gameEntity.getCurrentPlayer() == PlayerNumber.ONE ? firstPlayer : secondPlayer;
        List<BorderStoneDTO> borderStoneDTOList = mapBorderStones(gameEntity);
        return new Game(
                gameEntity.getGameUUID(),
                cardsInDeck,
                firstPlayer,
                secondPlayer,
                gameEntity.getCurrentTurn(),
                currentPlayer,
                borderStoneDTOList);
    }

    private @NotNull List<BorderStoneDTO> mapBorderStones(GameEntity gameEntity) {
        return gameEntity.getBorderStones().stream()
                .map(this::mapBorderStoneEntity)
                .collect(Collectors.toList());
    }

    private BorderStoneDTO mapBorderStoneEntity(BorderStoneEntity borderStoneEntity) {
        EnumMap<PlayerNumber, List<Card>> playedCards = new EnumMap<>(PlayerNumber.class);
        playedCards.put(PlayerNumber.ONE, getCardsForPlayer(borderStoneEntity, PlayerNumber.ONE));
        playedCards.put(PlayerNumber.TWO, getCardsForPlayer(borderStoneEntity, PlayerNumber.TWO));
        return new BorderStoneDTO(playedCards, borderStoneEntity.getFirstCompletion(), borderStoneEntity.getStoneNumber());
    }

    @NotNull
    private List<Card> getCardsForPlayer(BorderStoneEntity borderStoneEntity, PlayerNumber playerNumber) {
        return borderStoneEntity.getPlayedCards().stream()
                .filter(playedCardEntity -> playedCardEntity.getPlayerNumber() == playerNumber)
                .map(PlayedCardEntity::getCard)
                .sorted()
                .map(CardEntity::toCard)
                .collect(Collectors.toList());
    }

    private Player getPlayer(GameEntity gameEntity, Predicate<PlayerEntity> predicate) {
        return gameEntity.getPlayers().stream()
                .filter(predicate).map(PlayerEntity::toPlayer).findFirst().orElse(null);
    }

    private List<Card> getCardsInDeck(GameEntity gameEntity) {
        return gameEntity.getCards().stream()
                .filter(CardEntity::isInDeck)
                .sorted()
                .map(CardEntity::toCard)
                .collect(toList());
    }
}
