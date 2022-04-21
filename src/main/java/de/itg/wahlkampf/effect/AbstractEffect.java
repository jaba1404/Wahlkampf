package de.itg.wahlkampf.effect;

import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.TimeHelper;

public abstract class AbstractEffect {
    private long duration;
    private final String name;
    private final AbstractPlayerObject affectedPlayer;
    private final boolean deleteImmediately;
    private boolean toBeRemoved;

    private final TimeHelper timeHelper;

    public AbstractEffect(String name, long duration, AbstractPlayerObject affectedPlayer) {
        this.timeHelper = new TimeHelper();
        this.duration = duration;
        this.name = name;
        this.affectedPlayer = affectedPlayer;
        this.deleteImmediately = false;
        timeHelper.reset();
    }

    public AbstractEffect(String name, AbstractPlayerObject affectedPlayer) {
        this.timeHelper = null;
        this.duration = 0;
        this.name = name;
        this.affectedPlayer = affectedPlayer;
        this.deleteImmediately = true;
    }

    public abstract void onEffect();

    protected abstract void onStop();

    public boolean isDeleteImmediately() {
        return deleteImmediately;
    }

    public boolean isToBeRemoved() {
        return toBeRemoved;
    }

    public void setToBeRemoved(boolean toBeRemoved) {
        this.toBeRemoved = toBeRemoved;
    }

    public long getDuration() {
        return duration;
    }

    public String getName() {
        return name;
    }

    public AbstractPlayerObject getAffectedPlayer() {
        return affectedPlayer;
    }

    public TimeHelper getTimeHelper() {
        return timeHelper;
    }

    public void removeEffect() {
        onStop();
        toBeRemoved = true;
    }
}
