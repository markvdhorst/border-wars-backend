package com.markvdhorst.game.domain;

import com.markvdhorst.game.domain.gamewincalculation.BorderStoneWinStateAdapter;
import com.markvdhorst.game.domain.gamewincalculation.GameWinCalculator;
import com.markvdhorst.game.domain.gamewincalculation.StoneWinState;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

class Board {
    private static final int BORDER_STONE_COUNT = 9;
    private final SortedSet<BorderStone> borderStones;

    Board() {
        borderStones = new TreeSet<>(Comparator.comparingInt(BorderStone::getStoneNumber));
        for (int stoneNumber = 1; stoneNumber <= BORDER_STONE_COUNT; stoneNumber++) {
            borderStones.add(new BorderStone(stoneNumber));
        }
    }

    public Board(List<BorderStoneDTO> borderStoneDTOList) {
        borderStones = new TreeSet<>(Comparator.comparingInt(BorderStone::getStoneNumber));
        for (BorderStoneDTO borderStoneDTO : borderStoneDTOList) {
            borderStones.add(new BorderStone(borderStoneDTO.getStoneNumber(), borderStoneDTO.getFirstCompletion(), borderStoneDTO.getPlayedCards()));
        }
    }

    void playCard(int stoneNumber, PlayerNumber playerNumber, Card card) {
        BorderStone borderStone = getStone(stoneNumber);
        borderStone.playCard(playerNumber, card);
    }

    private BorderStone getStone(int stoneNumber) {
        return borderStones.stream()
                .filter(stone -> stone.getStoneNumber() == stoneNumber)
                .findFirst()
                .orElseThrow();
    }

    Set<BorderStone> getBorderStones() {
        return borderStones;
    }

    Optional<PlayerNumber> getWinner() {
        Function<BorderStone, StoneWinState> mapper = makeWinStateAdapter(getVisibleCards());
        return new GameWinCalculator().determineWinner(borderStones.stream().map(mapper).collect(toList()));
    }

    private Function<BorderStone, StoneWinState> makeWinStateAdapter(List<Card> visibleCards) {
        return borderStone -> new BorderStoneWinStateAdapter(borderStone, visibleCards);
    }

    List<Card> getVisibleCards() {
        return borderStones.stream()
                .map(BorderStone::getPlayedCards)
                .map(Map::values)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .collect(toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Board board = (Board) o;
        return borderStones.equals(board.borderStones);
    }

    @Override
    public int hashCode() {
        return Objects.hash(borderStones);
    }

    @Override
    public String toString() {
        return "Board{" +
                "borderStones=" + borderStones +
                '}';
    }
}
