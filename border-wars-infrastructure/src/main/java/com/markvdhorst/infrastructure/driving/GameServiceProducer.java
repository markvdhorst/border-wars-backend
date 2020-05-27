package com.markvdhorst.infrastructure.driving;

import com.markvdhorst.game.api.GameService;
import com.markvdhorst.game.domain.GameServiceImpl;
import com.markvdhorst.infrastructure.driven.InMemoryGameRepository;
import com.markvdhorst.infrastructure.driven.jpa.JpaGameRepository;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

@RequestScoped
public class GameServiceProducer {

    @EJB
    private JpaGameRepository jpaGameRepository;

    @Inject
    private InMemoryGameRepository inMemoryGameRepository;

    @Produces
    @RequestScoped
    public GameService getGameService() {
        return new GameServiceImpl(jpaGameRepository);
    }
}
