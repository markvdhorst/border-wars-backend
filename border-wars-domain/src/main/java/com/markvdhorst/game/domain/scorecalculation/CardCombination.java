package com.markvdhorst.game.domain.scorecalculation;

import com.markvdhorst.game.domain.Card;

import java.util.List;
import java.util.Objects;

class CardCombination {
    private final Card firstCard;
    private final Card secondCard;
    private final Card thirdCard;

    CardCombination(Card firstCard, Card secondCard, Card thirdCard) {
        this.firstCard = Objects.requireNonNull(firstCard);
        this.secondCard = Objects.requireNonNull(secondCard);
        this.thirdCard = Objects.requireNonNull(thirdCard);
    }

    boolean containsCards(List<Card> incompleteCombination) {
        for (Card card : incompleteCombination) {
            if(!containsCard(card)) {
                return false;
            }
        }

        return true;
    }

    private boolean containsCard(Card card) {
        return firstCard.equals(card) || secondCard.equals(card) || thirdCard.equals(card);
    }

    boolean containsAnyCardOf(List<Card> cards) {
        return cards.stream().anyMatch(this::containsCard);
    }

    @SuppressWarnings("ObjectEquality")
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardCombination that = (CardCombination) o;
        return firstCard.equals(that.firstCard) &&
                secondCard.equals(that.secondCard) &&
                thirdCard.equals(that.thirdCard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstCard, secondCard, thirdCard);
    }

    @Override
    public String toString() {
        return "CardCombination{" +
                "firstCard=" + firstCard +
                ", secondCard=" + secondCard +
                ", thirdCard=" + thirdCard +
                '}';
    }
}
