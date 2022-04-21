package de.itg.wahlkampf.effect.effects;

import de.itg.wahlkampf.effect.AbstractEffect;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.TimeHelper;

public class RegenerationEffect extends AbstractEffect {
    private final int increment;
    private final long delay;
    private final TimeHelper timeHelper;

    public RegenerationEffect(int increment, long delay, long duration, AbstractPlayerObject affectedPlayer) {
        super("Regeneration", duration, affectedPlayer);
        this.increment = increment;
        this.delay = delay;
        timeHelper = new TimeHelper();
    }

    @Override
    public void onEffect() {
        if(timeHelper.hasPassed(delay)) {
            if(getAffectedPlayer().getHealthPoints() < getAffectedPlayer().getBaseHealthPoints() - increment) {
                getAffectedPlayer().setHealthPoints(getAffectedPlayer().getHealthPoints() + increment);
                timeHelper.reset();
            }else{
                getAffectedPlayer().setHealthPoints(getAffectedPlayer().getBaseHealthPoints());
                setToBeRemoved(true);
            }
        }
    }

    @Override
    protected void onStop() {

    }
}
