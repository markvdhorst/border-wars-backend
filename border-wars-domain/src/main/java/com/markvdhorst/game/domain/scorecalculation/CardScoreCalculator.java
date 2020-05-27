package com.markvdhorst.game.domain.scorecalculation;

import com.markvdhorst.game.domain.Card;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

public enum CardScoreCalculator {
    INSTANCE;

    private final Map<CardCombination, Integer> cardCombinationScoreMap;

    CardScoreCalculator() {
        cardCombinationScoreMap = new HashMap<>();

        List<Card> allCards = Card.getAllCards();

        for (Card firstCard : allCards) {
            for (Card secondCard : allCards) {
                if (secondCard.equals(firstCard)) {
                    continue;
                }
                for (Card thirdCard : allCards) {
                    if (thirdCard.equals(secondCard) || thirdCard.equals(firstCard)) {
                        continue;
                    }
                    cardCombinationScoreMap.put(new CardCombination(firstCard, secondCard, thirdCard), calculateScore(List.of(firstCard, secondCard, thirdCard)));
                }
            }
        }
    }

    public int getScore(List<Card> cards) {
        if (cards.size() != 3) {
            throw new IllegalArgumentException("Can only calculate the score of 3 cards");
        }

        return cardCombinationScoreMap.get(new CardCombination(cards.get(0), cards.get(1), cards.get(2)));
    }


    public boolean finishedPlayerHasBestCombination(List<Card> completeCombination, List<Card> incompleteCombination, List<Card> visibleCards) {
        int score = getScore(completeCombination);

        List<Card> visibleCardsExceptForIncompleteCombination = visibleCards.stream()
                .filter(not(incompleteCombination::contains))
                .collect(Collectors.toList());

        Optional<Map.Entry<CardCombination, Integer>> betterCombination = cardCombinationScoreMap.entrySet().stream()
                .filter(scoreHigherThan(score))
                .filter(containsCards(incompleteCombination))
                .filter(doesNotContainAnyCardIn(visibleCardsExceptForIncompleteCombination))
                .findAny();
        return betterCombination.isEmpty();
    }

    private Predicate<? super Map.Entry<CardCombination, Integer>> doesNotContainAnyCardIn(List<Card> cards) {
        return not(entry -> entry.getKey().containsAnyCardOf(cards));
    }

    private Predicate<? super Map.Entry<CardCombination, Integer>> containsCards(List<Card> incompleteCombination) {
        return entry -> entry.getKey().containsCards(incompleteCombination);
    }

    private Predicate<Map.Entry<CardCombination, Integer>> scoreHigherThan(int score) {
        return entry -> entry.getValue() > score;
    }

    private int calculateScore(List<Card> cards) {
        return calculateBonusPoints(cards) + getSum(cards);
    }

    private int calculateBonusPoints(List<Card> cards) {
        if (allTheSameColor(cards) && threeCardsInARow(cards)) {
            return 400;
        }

        if (allTheSameNumber(cards)) {
            return 300;
        }

        if (allTheSameColor(cards)) {
            return 200;
        }

        if (threeCardsInARow(cards)) {
            return 100;
        }

        return 0;
    }

    private boolean threeCardsInARow(List<Card> cards) {
        List<Integer> sortedCardNumbers = cards.stream()
                .map(Card::getNumber)
                .sorted()
                .collect(Collectors.toList());
        return (sortedCardNumbers.get(1) - sortedCardNumbers.get(0) == 1
                && sortedCardNumbers.get(2) - sortedCardNumbers.get(1) == 1);
    }

    private int getSum(List<Card> cards) {
        return cards.stream().mapToInt(Card::getNumber).sum();
    }

    private boolean allTheSameNumber(List<Card> cards) {
        return cards.stream()
                .map(Card::getNumber)
                .collect(Collectors.toSet()).size() == 1;
    }

    private boolean allTheSameColor(List<Card> cards) {
        return cards.stream()
                .map(Card::getColor)
                .collect(Collectors.toSet()).size() == 1;
    }

}
