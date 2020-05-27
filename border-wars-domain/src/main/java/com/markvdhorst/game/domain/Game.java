package com.markvdhorst.game.domain;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game {

    private static final int MAX_NUMBER_OF_CARDS_IN_HAND = 6;
    private final Supplier<Deck> deckFactory;
    private final UUID gameUUID;

    private final Player firstPlayer;
    private Player secondPlayer;

    private Deck deck;
    private Board board;

    private Player currentPlayer;
    private int currentTurn;

    Game(Supplier<Deck> deckFactory, String playerOneName, PlayerUUID playerOneUUID) {
        this.deckFactory = deckFactory;
        firstPlayer = new Player(PlayerNumber.ONE, playerOneName, playerOneUUID);
        gameUUID = UUID.randomUUID();
    }

    Game(String playerOneName, PlayerUUID playerOneUUID) {
        this(Deck::shuffledDeck, playerOneName, playerOneUUID);
    }

    public Game(UUID gameUUID, List<Card> cardsInDeck, Player playerOne, Player playerTwo, int currentTurn, Player currentPlayer, List<BorderStoneDTO> borderStoneDTOList) {
        this.deck = cardsInDeck == null ? null : new Deck(cardsInDeck);
        this.firstPlayer = playerOne;
        this.secondPlayer = playerTwo;
        this.deckFactory = Deck::shuffledDeck;
        this.gameUUID = gameUUID;
        this.board = new Board(borderStoneDTOList);
        this.currentTurn = currentTurn;
        this.currentPlayer = currentPlayer;
    }

    public void addSecondPlayer(String secondPlayerName, PlayerUUID playerTwoUUID) {
        secondPlayer = new Player(PlayerNumber.TWO, secondPlayerName, playerTwoUUID);
    }

    public void startGame() {
        deck = deckFactory.get();
        board = new Board();
        currentTurn = 1;

        for (int i = 0; i < MAX_NUMBER_OF_CARDS_IN_HAND; i++) {
            firstPlayer.addCard(deck.takeNextCard().orElseThrow());
            secondPlayer.addCard(deck.takeNextCard().orElseThrow());
        }

        currentPlayer = firstPlayer;
    }

    public void playCard(PlayerUUID playerUUID, Card card, int borderStone) {
        ensurePlayerIsCurrentPlayer(getPlayerNumber(playerUUID));
        currentPlayer.playCard(board, borderStone, card);
        deck.takeNextCard().ifPresent(currentPlayer::addCard);
        switchTurn();
    }

    private void ensurePlayerIsCurrentPlayer(PlayerNumber playerNumber) {
        if (playerNumber != currentPlayer.getNumber()) {
            throw new IllegalArgumentException("cannot make a move if it's not your turn");
        }
    }

    private Player getPlayer(PlayerNumber playerNumber) {
        switch (playerNumber) {
            case ONE:
                return firstPlayer;
            case TWO:
                return secondPlayer;
            default:
                throw new IllegalArgumentException("Cannot get player for unknown playerNumber " + playerNumber);
        }
    }

    private void switchTurn() {
        currentTurn++;
        if (currentPlayer.equals(firstPlayer)) {
            currentPlayer = secondPlayer;
        } else if (currentPlayer.equals(secondPlayer)) {
            currentPlayer = firstPlayer;
        } else {
            throw new IllegalStateException("Current player is not first player or second player");
        }
    }

    public GameState getState(PlayerUUID playerUUID) {
        GameStatus status = getStatus();
        GameStateBuilder gameStateBuilder = new GameStateBuilder()
                .withGameStatus(status)
                .withTurn(currentTurn)
                .withYourPlayerNumber(getPlayerNumber(playerUUID))
                .withPlayerNames(getPlayerNames());
        if (status != GameStatus.WAITING_FOR_SECOND_PLAYER) {
            gameStateBuilder
                    .withCurrentPlayer(currentPlayer.getNumber())
                    .withMyHand(getHand(playerUUID))
                    .withWinner(getWinningPlayer().map(Player::getNumber).orElse(null))
                    .withStones(getStoneState());
        }

        return gameStateBuilder.createGameState();
    }

    private List<Card> getHand(PlayerUUID playerUUID) {
        return getPlayer(getPlayerNumber(playerUUID)).getCardsInHand();
    }

    private Map<PlayerNumber, String> getPlayerNames() {
        return getPlayers().stream()
                .collect(Collectors.toMap(Player::getNumber, Player::getName, (a, b) -> b, HashMap::new));
    }

    private PlayerNumber getPlayerNumber(PlayerUUID playerUUID) {
        if (firstPlayer.getPlayerUuid().equals(playerUUID)) {
            return firstPlayer.getNumber();
        } else if (secondPlayer.getPlayerUuid().equals(playerUUID)) {
            return secondPlayer.getNumber();
        } else {
            throw new IllegalArgumentException("Unknown playerUUID " + playerUUID);
        }
    }

    public GameStatus getStatus() {
        if (secondPlayer == null) {
            return GameStatus.WAITING_FOR_SECOND_PLAYER;
        } else if (getWinningPlayer().isPresent()) {
            return GameStatus.FINISHED;
        } else {
            return GameStatus.PLAYING;
        }
    }

    public List<Player> getPlayers() {
        return Stream.concat(
                Optional.ofNullable(firstPlayer).stream(),
                Optional.ofNullable(secondPlayer).stream()
        ).collect(Collectors.toList());
    }

    public UUID getGameUUID() {
        return gameUUID;
    }

    public Collection<Card> getCardsInDeck() {
        return deck == null ? Collections.emptyList() : deck.getCards();
    }

    private Optional<Player> getWinningPlayer() {
        return board.getWinner()
                .map(this::getPlayer);
    }

    private @NotNull List<StoneState> getStoneState() {
        return board.getBorderStones().stream()
                .map(this::mapBorderStone)
                .collect(Collectors.toList());
    }

    private StoneState mapBorderStone(BorderStone borderStone) {

        Map<PlayerNumber, List<Card>> playedCardsMap = new HashMap<>();
        for (Map.Entry<PlayerNumber, List<Card>> playerNumberListEntry : borderStone.getPlayedCards().entrySet()) {
            playedCardsMap.put(getPlayer(playerNumberListEntry.getKey()).getNumber(), playerNumberListEntry.getValue());
        }

        PlayerNumber winner = borderStone.getWinningPlayer(board.getVisibleCards()).orElse(null);
        return new StoneState(borderStone.getStoneNumber(), winner, playedCardsMap);
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public Set<BorderStone> getBorderStones() {
        if(board == null) {
             return null;
        }
        return board.getBorderStones();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Game game = (Game) o;
        return currentTurn == game.currentTurn &&
                Objects.equals(gameUUID, game.gameUUID) &&
                Objects.equals(firstPlayer, game.firstPlayer) &&
                Objects.equals(secondPlayer, game.secondPlayer) &&
                Objects.equals(deck, game.deck) &&
                Objects.equals(board, game.board) &&
                Objects.equals(currentPlayer, game.currentPlayer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameUUID, firstPlayer, secondPlayer, deck, board, currentPlayer, currentTurn);
    }

    @Override
    public String toString() {
        return "Game{" +
                "gameUUID=" + gameUUID +
                ", firstPlayer=" + firstPlayer +
                ", secondPlayer=" + secondPlayer +
                ", deck=" + deck +
                ", board=" + board +
                ", currentPlayer=" + currentPlayer +
                ", currentTurn=" + currentTurn +
                '}';
    }
}
