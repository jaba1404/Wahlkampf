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

    public float getRounded(float value, float accuracy) {
        final float factor = 1 / accuracy;
        return Math.round(value * factor) / factor;
    }

    public int getDistanceTo(AbstractGameObject from, AbstractGameObject to) {
        final int xDelta = (from.getPositionX() + from.getWidth() / 2) - (to.getPositionX() + to.getWidth() / 2);
        final int yDelta = (from.getPositionY() + from.getHeight() / 2) - (to.getPositionY() + to.getHeight() / 2);

        return (int) Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
    }

    public int interpolateValue(int start, int end, int pct) {
        return start + (end - start) * pct;
    }
}