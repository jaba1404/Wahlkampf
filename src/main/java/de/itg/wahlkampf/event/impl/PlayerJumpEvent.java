package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.Event;
import de.itg.wahlkampf.object.AbstractGameObject;

public class PlayerJumpEvent extends Event {
    private final AbstractGameObject gameObject;

    public PlayerJumpEvent(AbstractGameObject gameObject) {
        this.gameObject = gameObject;
    }

    public AbstractGameObject getGameObject() {
        return gameObject;
    }
}
