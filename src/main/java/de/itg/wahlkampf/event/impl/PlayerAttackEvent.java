package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.Event;
import de.itg.wahlkampf.object.AbstractGameObject;

public class PlayerAttackEvent extends Event {
    private final AbstractGameObject gameObject;

    public PlayerAttackEvent(AbstractGameObject gameObject) {
        this.gameObject = gameObject;
    }

    public AbstractGameObject getGameObject() {
        return gameObject;
    }
}
