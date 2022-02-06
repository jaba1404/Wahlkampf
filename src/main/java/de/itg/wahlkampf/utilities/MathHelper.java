package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.object.AbstractGameObject;

import java.util.Random;

public class MathHelper {

    private static final Random RANDOM = new Random();

    public int clampInt(int num, int min, int max) {
        return num < min ? min : (Math.min(num, max));
    }

    public float clampFloat(float num, float min, float max) {
        return num < min ? min : (Math.min(num, max));
    }

    public double clampDouble(double num, double min, double max) {
        return num < min ? min : (Math.min(num, max));
    }

    public int getRandomInt(int min, int max) {
        return min >= max ? min : RANDOM.nextInt(max - min + 1) + min;
    }

    public float getRandomFloat(float min, float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }

    public float getDistanceTo(AbstractGameObject from, AbstractGameObject to) {
        final float xDelta = from.getPositionX() - to.getPositionX();
        final float yDelta = from.getPositionY() - to.getPositionY();

        return (float) Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
    }
    public int interpolateValue(int start, int end, int pct)
    {
        return start + (end - start) * pct;
    }
}