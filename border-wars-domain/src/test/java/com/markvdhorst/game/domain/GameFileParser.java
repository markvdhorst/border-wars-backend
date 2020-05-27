package com.markvdhorst.game.domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class GameFileParser {

    private List<Turn> turns;

    public GameFileParser(String gameFileName) {
        parseGameFiles(gameFileName);
    }

    private void parseGameFiles(String gameFileName) {
        URL gameFileUrl = getGameFileUrl(gameFileName);
        try (var inputStream = gameFileUrl.openStream();
             var inputStreamReader = new InputStreamReader(inputStream);
             var bufferedReader = new BufferedReader(inputStreamReader)) {
            this.turns = parseTurns(bufferedReader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Turn> parseTurns(BufferedReader reader) throws IOException {
        String rawLine;

        List<Turn> turns = new ArrayList<>();
        TurnBuilder turnBuilder = null;
        while ((rawLine = reader.readLine()) != null) {
            String filteredLine = filterCommentsAndTrim(rawLine);
            if (filteredLine.isBlank()) {
                continue;
            }

            String[] symbols = filteredLine.split("\\s");
            switch (symbols[0].toUpperCase()) {
                case "MOVE":
                    if (turnBuilder != null) {
                        turns.add(turnBuilder.build());
                    }
                    turnBuilder = new TurnBuilder(parseMove(symbols));
                    break;
                case "STONEWIN":
                    turnBuilder.addExpectedStoneWin(parseStoneWin(symbols));
                    break;
                case "GAMEWIN":
                    turnBuilder.addExpectedGameWin(parseGameWin(symbols));
                    break;
            }
        }

        if (turnBuilder != null) {
            turns.add(turnBuilder.build());
        }
        return turns;
    }

    private GameWin parseGameWin(String[] symbols) {
        PlayerNumber playerNumber = parsePlayerNumber(symbols[1]);
        return new GameWin(playerNumber);
    }

    private StoneWin parseStoneWin(String[] symbols) {
        int stone = parseStone(symbols[1]);
        PlayerNumber playerNumber = parsePlayerNumber(symbols[2]);
        return new StoneWin(stone, playerNumber);
    }

    private int parseStone(String symbol) {
        return Integer.parseInt(symbol.substring(1)); //B4 = 4
    }

    private Move parseMove(String[] symbols) {

        PlayerNumber player = parsePlayerNumber(symbols[1]);
        Card playedCard = parseCard(symbols[2], symbols[3]);
        int stone = parseStone(symbols[4]);
        return new Move(player, playedCard, stone);
    }

    private Card parseCard(String cardColor, String cardNumber) {
        Card.Color color = parseColor(cardColor);
        int number = parseCardNumber(cardNumber);
        return new Card(color, number);
    }

    private Card.Color parseColor(String cardColor) {
        return Card.Color.valueOf(cardColor.toUpperCase());
    }

    private int parseCardNumber(String cardNumber) {
        return Integer.parseInt(cardNumber);
    }

    private PlayerNumber parsePlayerNumber(String playerNumberString) {
        switch (playerNumberString.toUpperCase()) {
            case "P1":
                return PlayerNumber.ONE;
            case "P2":
                return PlayerNumber.TWO;
            default:
                throw new IllegalArgumentException("unknown player number:" + playerNumberString);
        }
    }

    private String filterCommentsAndTrim(String rawLine) {
        return rawLine;
    }

    public List<Turn> getTurns() {
        return turns;
    }

    private URL getGameFileUrl(String gameFileName) {
        URL gameFileUrl = getClass().getClassLoader().getResource(getGameFilePath(gameFileName));
        if (gameFileUrl == null) {
            throw new IllegalArgumentException(String.format("no game file found at path: %s ", getGameFilePath(gameFileName)));
        }
        return gameFileUrl;
    }

    private String getGameFilePath(String gameFileName) {
        return String.format("com/markvdhorst/game/gamefiles/%s.txt", gameFileName);
    }

}
