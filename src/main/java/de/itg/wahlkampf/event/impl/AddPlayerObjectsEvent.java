package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.AbstractEvent;
import de.itg.wahlkampf.object.AbstractGameObject;

import java.util.List;

public class AddPlayerObjectsEvent extends AbstractEvent {
    private final List<AbstractGameObject> abstractGameObjects;

    public AddPlayerObjectsEvent(List<AbstractGameObject> abstractGameObjects) {
        this.abstractGameObjects = abstractGameObjects;
    }

    public List<AbstractGameObject> getAbstractGameObjects() {
        return abstractGameObjects;
    }
}
