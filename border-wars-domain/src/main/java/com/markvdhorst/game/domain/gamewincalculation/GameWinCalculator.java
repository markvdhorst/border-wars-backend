package com.markvdhorst.game.domain.gamewincalculation;

import com.markvdhorst.game.domain.PlayerNumber;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GameWinCalculator {
    public Optional<PlayerNumber> determineWinner(List<StoneWinState> stoneWinStateList) {

        return getPlayerWithThreeStonesInARow(stoneWinStateList)
                .or(() -> getPlayerWithFiveStones(stoneWinStateList));
    }

    private Optional<PlayerNumber> getPlayerWithThreeStonesInARow(List<StoneWinState> stoneWinStateList) {
        ArrayList<StoneWinState> sortedWinStateList = toListSortedByStoneNumber(stoneWinStateList);

        WinnerCounter winnerCounter = new WinnerCounter();
        for (StoneWinState borderStone : sortedWinStateList) {
            Optional<PlayerNumber> winner = borderStone.getWinner();
            if (winner.isEmpty()) {
                winnerCounter.clearWinner();
            } else {
                winnerCounter.setOrIncrementWinner(winner.get());
            }
            if (winnerCounter.winnerHasThreeInARow()) {
                return Optional.of(winnerCounter.getLastWinner());
            }
        }
        return Optional.empty();
    }
    private @NotNull ArrayList<StoneWinState> toListSortedByStoneNumber(List<StoneWinState> sortedWinState) {
        ArrayList<StoneWinState> stoneWinStates = new ArrayList<>(sortedWinState);
        stoneWinStates.sort(Comparator.comparingInt(StoneWinState::getStoneNumber));
        return stoneWinStates;
    }

    private Optional<PlayerNumber> getPlayerWithFiveStones(List<StoneWinState> sortedWinState) {
        return Arrays.stream(PlayerNumber.values())
                .map(playerNumber -> new PlayerWins(playerNumber, getWins(sortedWinState, playerNumber)))
                .filter(PlayerWins::hasFiveWins)
                .map(PlayerWins::getPlayer)
                .findFirst();
    }

    private long getWins(List<StoneWinState> winStates, PlayerNumber playerNumber) {
        return winStates.stream()
                .map(StoneWinState::getWinner)
                .flatMap(Optional::stream)
                .filter(player -> player == playerNumber)
                .count();
    }

    private static class PlayerWins {
        final PlayerNumber player;
        final long wins;

        PlayerWins(PlayerNumber player, long wins) {
            this.player = player;
            this.wins = wins;
        }

        boolean hasFiveWins() {
            return wins >= 5;
        }

        PlayerNumber getPlayer() {
            return player;
        }
    }
}
