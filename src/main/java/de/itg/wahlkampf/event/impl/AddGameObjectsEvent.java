package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.AbstractEvent;
import de.itg.wahlkampf.object.AbstractGameObject;

import java.util.List;

public class AddGameObjectsEvent extends AbstractEvent {
    private final List<AbstractGameObject> playerObjects;

    public AddGameObjectsEvent(List<AbstractGameObject> playerObjects) {
        this.playerObjects = playerObjects;
    }

    public List<AbstractGameObject> getPlayerObjects() {
        return playerObjects;
    }
}
