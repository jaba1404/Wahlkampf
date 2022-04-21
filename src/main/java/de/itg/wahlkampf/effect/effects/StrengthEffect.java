package de.itg.wahlkampf.effect.effects;

import de.itg.wahlkampf.effect.AbstractEffect;
import de.itg.wahlkampf.object.AbstractPlayerObject;

public class StrengthEffect extends AbstractEffect {
    private final int baseStrength;

    public StrengthEffect(int percentage, long duration, AbstractPlayerObject affectedPlayer) {
        super("Strength", duration, affectedPlayer);
        this.baseStrength = getAffectedPlayer().getBaseStrength();
        getAffectedPlayer().setStrength(baseStrength + (baseStrength * percentage / 100));
    }

    @Override
    public void onEffect() {
    }

    @Override
    protected void onStop() {
        getAffectedPlayer().setStrength(baseStrength);
    }
}
