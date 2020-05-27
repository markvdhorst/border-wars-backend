package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameToEntityMapper {

    public void updateGameEntityWithGame(Game game, GameEntity gameEntity) {
        updateGameEntity(game, gameEntity);
        updatePlayers(gameEntity, game.getPlayers());
        updateCardsInDeck(gameEntity, game.getCardsInDeck());
        updateBorderStones(gameEntity, game.getBorderStones());
    }

    private void updateBorderStones(GameEntity gameEntity, Set<BorderStone> borderStones) {
        if(borderStones == null) {
            return;
        }
        for (BorderStone borderStone : borderStones) {
            BorderStoneEntity borderStoneEntity = getOrCreateBorderStoneEntity(gameEntity, borderStone);
            borderStoneEntity.setFirstCompletion(borderStone.getFirstCompletion());
            updatePlayedCardsOnBorderStone(gameEntity, borderStone, borderStoneEntity);
        }
    }

    private void updatePlayedCardsOnBorderStone(GameEntity gameEntity, BorderStone borderStone, BorderStoneEntity borderStoneEntity) {
        Map<PlayerNumber, List<Card>> playedCardsByPlayer = borderStone.getPlayedCards();
        for (Map.Entry<PlayerNumber, List<Card>> cardsForPlayer : playedCardsByPlayer.entrySet()) {
            addCardIfItDoesNotExistOnBorderStone(gameEntity, borderStoneEntity, cardsForPlayer);
        }
    }

    private void addCardIfItDoesNotExistOnBorderStone(GameEntity gameEntity, BorderStoneEntity borderStoneEntity, Map.Entry<PlayerNumber, List<Card>> cardsForPlayer) {
        for (Card card : cardsForPlayer.getValue()) {
            if (doesNotExistOnBorderStone(borderStoneEntity, card)) {
                CardEntity cardOnPlayer = findCardOnPlayer(gameEntity, cardsForPlayer, card);
                borderStoneEntity.addPlayedCard(cardsForPlayer.getKey(), cardOnPlayer);
            }
        }
    }

    private boolean doesNotExistOnBorderStone(BorderStoneEntity borderStoneEntity, Card card) {
        return borderStoneEntity.getPlayedCards().stream()
                .noneMatch(playedCardEntity -> playedCardEntity.getCard().equalValue(card));
    }

    private CardEntity findCardOnPlayer(GameEntity gameEntity, Map.Entry<PlayerNumber, List<Card>> playerNumberListEntry, Card card) {
        PlayerEntity playerEntity = findPlayerEntity(gameEntity, playerNumberListEntry.getKey());
        return playerEntity.getCards().stream().filter(cardEntity -> cardEntity.equalValue(card)).findFirst().orElseThrow();
    }

    private PlayerEntity findPlayerEntity(GameEntity gameEntity, PlayerNumber playerNumber) {
        return gameEntity.getPlayers().stream()
                .filter(playerEntity -> playerEntity.getPlayerNumber() == playerNumber)
                .findFirst()
                .orElse(null);
    }

    private BorderStoneEntity getOrCreateBorderStoneEntity(GameEntity gameEntity, BorderStone borderStone) {
        return gameEntity.getBorderStones().stream()
                .filter(borderStoneEntity -> borderStoneEntity.getStoneNumber() == borderStone.getStoneNumber())
                .findFirst().orElseGet(() -> createAndAddNewBorderStone(gameEntity, borderStone.getStoneNumber()));
    }

    private BorderStoneEntity createAndAddNewBorderStone(GameEntity gameEntity, int stoneNumber) {
        BorderStoneEntity borderStoneEntity = new BorderStoneEntity();
        borderStoneEntity.setStoneNumber(stoneNumber);
        gameEntity.addBorderStone(borderStoneEntity);
        return borderStoneEntity;
    }

    private void updateGameEntity(Game game, GameEntity gameEntity) {
        gameEntity.setGameUUID(game.getGameUUID());
        Player currentPlayer = game.getCurrentPlayer();
        if (currentPlayer != null) {
            gameEntity.setCurrentPlayer(currentPlayer.getNumber());
        }
        gameEntity.setCurrentTurn(game.getCurrentTurn());
    }

    private void updateCardsInDeck(GameEntity gameEntity, Collection<Card> cardsInDeck) {
        List<Card> cardInDeckList = new ArrayList<>(cardsInDeck);
        for (int index = 0; index < cardInDeckList.size(); index++) {
            CardEntity cardEntity = getCardFromDeckOrCreateCard(gameEntity, cardInDeckList.get(index));
            cardEntity.setPosition(index);
        }
    }

    private void updatePlayers(GameEntity gameEntity, List<Player> players) {
        for (Player player : players) {
            PlayerEntity playerEntity = createOrUpdatePlayerEntity(gameEntity, player);
            updatePlayerCards(player.getCardsInHand(), gameEntity, playerEntity);
        }
    }

    private PlayerEntity createOrUpdatePlayerEntity(GameEntity gameEntity, Player player) {
        PlayerEntity playerEntity = getOrCreatePlayerEntity(gameEntity, player);
        return updatePlayerEntity(playerEntity, player);
    }

    @NotNull
    private PlayerEntity getOrCreatePlayerEntity(GameEntity gameEntity, Player player) {
        return gameEntity.getPlayers().stream()
                .filter(playerEntity -> playerEntity.getPlayerUUID().equals(player.getPlayerUuid().getPlayerUUID()))
                .findFirst().orElseGet(() -> addNewPlayerEntity(gameEntity));
    }

    @NotNull
    private PlayerEntity addNewPlayerEntity(GameEntity gameEntity) {
        PlayerEntity playerEntity1 = new PlayerEntity();
        gameEntity.addPlayer(playerEntity1);
        return playerEntity1;
    }

    private void updatePlayerCards(List<Card> playerCards, GameEntity gameEntity, PlayerEntity playerEntity) {
        for (int position = 0; position < playerCards.size(); position++) {
            CardEntity cardEntity = getCardFromDeckOrCreateCard(gameEntity, playerCards.get(position));
            if (!Objects.equals(cardEntity.getPlayer(), playerEntity)) {
                playerEntity.addCard(cardEntity);
            }
            cardEntity.setPosition(position);
        }
    }

    private CardEntity getCardFromDeckOrCreateCard(GameEntity gameEntity, Card playerCard) {
        return gameEntity.getCards().stream()
                .filter(cardEntity -> cardEntity.equalValue(playerCard))
                .findFirst()
                .orElseGet(() -> createCardAndAddToGame(gameEntity, playerCard));
    }

    private CardEntity createCardAndAddToGame(GameEntity gameEntity, Card playerCard) {
        CardEntity cardEntity = new CardEntity();
        cardEntity.setNumber(playerCard.getNumber());
        cardEntity.setColor(playerCard.getColor());
        gameEntity.addCard(cardEntity);
        return cardEntity;
    }

    private PlayerEntity updatePlayerEntity(PlayerEntity playerEntity, Player player) {
        playerEntity.setPlayerUUID(player.getPlayerUuid().getPlayerUUID());
        playerEntity.setPlayerName(player.getName());
        playerEntity.setPlayerNumber(player.getNumber());
        return playerEntity;
    }
}
