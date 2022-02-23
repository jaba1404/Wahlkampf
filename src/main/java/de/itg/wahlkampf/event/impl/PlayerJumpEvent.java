package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.AbstractEvent;
import de.itg.wahlkampf.object.AbstractGameObject;

public class PlayerJumpEvent extends AbstractEvent {
    private final AbstractGameObject gameObject;

    public PlayerJumpEvent(AbstractGameObject gameObject) {
        this.gameObject = gameObject;
    }

    public AbstractGameObject getGameObject() {
        return gameObject;
    }
}
