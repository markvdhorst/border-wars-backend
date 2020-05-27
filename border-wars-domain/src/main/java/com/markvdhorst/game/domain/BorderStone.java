package com.markvdhorst.game.domain;

import com.markvdhorst.game.domain.scorecalculation.CardScoreCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

import static java.lang.String.format;
import static java.util.Comparator.comparingInt;
import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;

public class BorderStone {

    private static final int MAX_CARDS_PER_STONE = 3;

    private final int stoneNumber;
    private final Map<PlayerNumber, List<Card>> playedCards;
    private PlayerNumber firstCompletion;

    BorderStone(int stoneNumber) {
        this.stoneNumber = stoneNumber;
        playedCards = new EnumMap<>(PlayerNumber.class);
        for (PlayerNumber value : PlayerNumber.values()) {
            playedCards.put(value, new ArrayList<>());
        }
    }

    public BorderStone(int stoneNumber, PlayerNumber firstCompletion, Map<PlayerNumber, List<Card>> playedCards) {
        this.stoneNumber = stoneNumber;
        this.playedCards = new EnumMap<>(PlayerNumber.class);
        this.playedCards.putAll(playedCards);
    }

    void playCard(PlayerNumber playerNumber, Card card) {
        requireNonNull(playerNumber, "playedNumber required to play a card");
        requireNonNull(card);
        checkStoneNotFull(playerNumber);
        playedCards.get(playerNumber).add(card);
        if(firstCompletion == null && playedCards.get(playerNumber).size() == MAX_CARDS_PER_STONE) {
            firstCompletion = playerNumber;
        }
    }

    private void checkStoneNotFull(PlayerNumber playerNumber) {
        if (playedCards.get(playerNumber).size() >= MAX_CARDS_PER_STONE) {
            throw new IllegalArgumentException(format("Cannot play a card on a stone that's full for player %s", playerNumber));
        }
    }

    public int getStoneNumber() {
        return stoneNumber;
    }

    /**
     * Calculates which player has won this border stone
     *
     * @return empty optional in case no player has won yet.
     */
    public Optional<PlayerNumber> getWinningPlayer(List<Card> visibleCards) {
        return calculateWinningPlayer(visibleCards);
    }

    private Optional<PlayerNumber> calculateWinningPlayer(List<Card> visibleCards) {
        if (isComplete()) {
            return getPlayerWithHighestScore();
        } else if (onePlayerHasFinished()) {
            return getFinishedPlayerIfCombinationIsUnbeatable(visibleCards);
        } else {
            return Optional.empty();
        }
    }


    private Optional<PlayerNumber> getFinishedPlayerIfCombinationIsUnbeatable(List<Card> visibleCards) {
        ensureExactlyOneFinishedPlayer();

        List<Card> completeCombination = playedCards.values().stream()
                .filter(isCompleteCombination())
                .findFirst()
                .orElseThrow();

        List<Card> incompleteCombination = playedCards.values().stream()
                .filter(cards -> cards.size() < MAX_CARDS_PER_STONE)
                .findFirst()
                .orElseThrow();

        if (CardScoreCalculator.INSTANCE.finishedPlayerHasBestCombination(completeCombination, incompleteCombination, visibleCards)) {
            return getPlayerWithCompleteHand();
        } else {
            return Optional.empty();
        }
    }

    private void ensureExactlyOneFinishedPlayer() {
        if (playedCards.values().stream().filter(isCompleteCombination()).count() != 1) {
            throw new IllegalStateException("Cannot use this method when both players have finished their hands");
        }
    }

    private @NotNull Predicate<List<Card>> isCompleteCombination() {
        return cards -> cards.size() == MAX_CARDS_PER_STONE;
    }

    private @NotNull Optional<PlayerNumber> getPlayerWithCompleteHand() {
        return of(playedCards.entrySet().stream()
                .filter(entry -> isCompleteCombination().test(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow()); // Or Else throw to ensure this method finds a player
    }


    private boolean onePlayerHasFinished() {
        return playedCards.values().stream().anyMatch(isCompleteCombination());
    }

    private Optional<PlayerNumber> getPlayerWithHighestScore() {
        return playedCards.entrySet().stream()
                .map(this::calculateScore)
                .max(comparingInt(PlayerScore::getPlayerScore).thenComparing(firstCompletion(firstCompletion)))
                .map(PlayerScore::getPlayerNumber);
    }

    private boolean isComplete() {
        return playedCards.values().stream().allMatch(isCompleteCombination());
    }

    private PlayerScore calculateScore(Map.Entry<PlayerNumber, List<Card>> cards) {
        return new PlayerScore(cards.getKey(), CardScoreCalculator.INSTANCE.getScore(cards.getValue()));
    }

    public Map<PlayerNumber, List<Card>> getPlayedCards() {
        return playedCards;
    }

    public PlayerNumber getFirstCompletion() {
        return firstCompletion;
    }

    @SuppressWarnings("ObjectEquality")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BorderStone that = (BorderStone) o;
        return stoneNumber == that.stoneNumber &&
                Objects.equals(playedCards, that.playedCards) &&
                firstCompletion == that.firstCompletion;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stoneNumber, playedCards, firstCompletion);
    }

    @Override
    public String toString() {
        return "BorderStone{" +
                "stoneNumber=" + stoneNumber +
                ", playedCards=" + playedCards +
                ", firstCompletion=" + firstCompletion +
                '}';
    }

    /**
     * Returns 1 if the first player is the one who completed the stone first
     * Returns -1 if the second player is the one who completed it first
     */
    private Comparator<PlayerScore> firstCompletion(PlayerNumber firstCompletion) {
        return (score1, score2) -> (score1.playerNumber == firstCompletion) ? 1
                : (score2.playerNumber == firstCompletion ? -1 : 0);
    }

    private static class PlayerScore {
        private final PlayerNumber playerNumber;
        private final Integer playerScore;

        public PlayerScore(PlayerNumber playerNumber, Integer playerScore) {
            this.playerNumber = playerNumber;
            this.playerScore = playerScore;
        }

        public PlayerNumber getPlayerNumber() {
            return playerNumber;
        }

        public Integer getPlayerScore() {
            return playerScore;
        }
    }
}
