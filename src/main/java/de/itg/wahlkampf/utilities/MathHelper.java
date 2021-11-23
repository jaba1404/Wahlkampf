package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.object.AbstractGameObject;

import java.util.Random;

public class MathHelper {

    private static final Random RANDOM = new Random();

    public static int clampInt(int num, int min, int max) {
        return num < min ? min : (Math.min(num, max));
    }

    public static float clampFloat(float num, float min, float max) {
        return num < min ? min : (Math.min(num, max));
    }

    public static double clampDouble(double num, double min, double max) {
        return num < min ? min : (Math.min(num, max));
    }

    public static int getRandomInt(int min, int max) {
        return min >= max ? min : RANDOM.nextInt(max - min + 1) + min;
    }

    public static float getRandomFloat(float min, float max) {
        return RANDOM.nextFloat() * (max - min) + min;
    }

    public float getDistanceTo(AbstractGameObject from, AbstractGameObject to) {
        final float xDelta = from.getPositionX() - to.getPositionX();
        final float yDelta = from.getPositionY() - to.getPositionY();

        return (float) Math.sqrt(Math.pow(xDelta, 2) + Math.pow(yDelta, 2));
    }
}