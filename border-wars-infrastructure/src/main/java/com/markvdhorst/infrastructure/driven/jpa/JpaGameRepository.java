package com.markvdhorst.infrastructure.driven.jpa;

import com.markvdhorst.game.domain.Game;
import com.markvdhorst.game.domain.PlayerUUID;
import com.markvdhorst.game.ports.GameRepository;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;
import java.util.UUID;

@Stateless
@LocalBean
public class JpaGameRepository implements GameRepository {

    @PersistenceContext(unitName = "gamePU")
    private EntityManager em;

    @Inject
    private GameToEntityMapper gameToEntityMapper;

    @Inject
    private EntityToGameMapper entityToGameMapper;

    @Override
    public Optional<Game> getGameWaitingForPlayer() {
        return findGameEntityWaitingForPlayer().map(entityToGameMapper::toGame);
    }

    @Override
    public void save(Game game) {
        GameEntity gameEntity = findGameEntityByGameUUID(game.getGameUUID());
        gameToEntityMapper.updateGameEntityWithGame(game, gameEntity);
    }

    @Override
    public void create(Game game) {
        GameEntity gameEntity = new GameEntity();
        gameToEntityMapper.updateGameEntityWithGame(game, gameEntity);
        em.persist(gameEntity);
    }

    @Override
    public Game findGameByPlayerUUID(PlayerUUID playerUUID) {
        return entityToGameMapper.toGame(findGameEntityByPlayerUUID(playerUUID.getPlayerUUID()));
    }

    private GameEntity findGameEntityByGameUUID(UUID gameUUID) {
        return em.createQuery(
                "select g" +
                        " from game g" +
                        " where g.gameUUID = :gameUUID",
                GameEntity.class)
                .setParameter("gameUUID", gameUUID)
                .getSingleResult();
    }

    private GameEntity findGameEntityByPlayerUUID(UUID playerUUID) {
        return em.createQuery(
                "select g" +
                        " from game g" +
                        " join g.players p" +
                        " where p.playerUUID = :playerUUID",
                GameEntity.class)
                .setParameter("playerUUID", playerUUID)
                .getSingleResult();
    }

    private Optional<GameEntity> findGameEntityWaitingForPlayer() {
        return em.createQuery(
                "select g" +
                        " from game g" +
                        " where size(players) = 1",
                GameEntity.class)
                .getResultList().stream().findFirst();
    }
}
