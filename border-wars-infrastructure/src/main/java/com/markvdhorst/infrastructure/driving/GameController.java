package com.markvdhorst.infrastructure.driving;

import com.markvdhorst.game.api.GameService;
import com.markvdhorst.game.api.PlayCardCommand;
import com.markvdhorst.game.domain.Card;
import com.markvdhorst.game.domain.PlayerUUID;
import com.markvdhorst.game.domain.GameState;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.stream.Collectors;

@Path("game")
public class GameController {

    @Inject
    private GameService gameService;

    @GET
    @Path("test")
    public String test() {
        PlayerUUID mark = gameService.register("Mark");
        PlayerUUID britta = gameService.register("Britta");
        return String.format("Mark's cards: <br/> %s <br/><br/> Britta's awesome cards: <br/> %s", getCards(mark), getCards(britta));
    }

    @POST
    @Path("register")
    @Produces("application/json")
    @Consumes("application/json")
    public PlayerUUID register(JsonName name) {
        return gameService.register(name.getPlayerName());
    }

    @GET
    @Path("{playerUUID}")
    @Produces("application/json")
    public GameState getState(@PathParam("playerUUID") PlayerUUID playerUUID) {
        return gameService.getGameState(playerUUID);
    }

    @POST
    @Path("{playerUUID}/playCard")
    @Consumes("application/json")
    public void playCard(@PathParam("playerUUID") PlayerUUID playerUUID, PlayCardCommand playCardCommand) {
        gameService.playCard(playerUUID, playCardCommand);
    }

    private @NotNull String getCards(PlayerUUID mark) {
        return gameService.getGameState(mark).getMyHand().stream()
                .map(Card::toString)
                .map(s -> " - " + s)
                .collect(Collectors.joining("<br/>"));
    }

}
