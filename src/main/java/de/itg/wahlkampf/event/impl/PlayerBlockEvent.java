package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.AbstractEvent;
import de.itg.wahlkampf.object.AbstractGameObject;

public class PlayerBlockEvent extends AbstractEvent {
    private final AbstractGameObject attackSource;
    private final AbstractGameObject blockingObject;

    public PlayerBlockEvent(AbstractGameObject attackSource, AbstractGameObject blockingObject) {
        this.attackSource = attackSource;
        this.blockingObject = blockingObject;
    }

    public AbstractGameObject getAttackSource() {
        return attackSource;
    }

    public AbstractGameObject getBlockingObject() {
        return blockingObject;
    }
}
