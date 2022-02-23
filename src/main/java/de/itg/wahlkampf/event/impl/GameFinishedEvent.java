package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.AbstractEvent;
import de.itg.wahlkampf.object.AbstractGameObject;

public class GameFinishedEvent extends AbstractEvent {

    private AbstractGameObject winner;

    public GameFinishedEvent(AbstractGameObject winner) {
        this.winner = winner;
    }

    public AbstractGameObject getWinner() {
        return winner;
    }

    public void setWinner(AbstractGameObject winner) {
        this.winner = winner;
    }
}
