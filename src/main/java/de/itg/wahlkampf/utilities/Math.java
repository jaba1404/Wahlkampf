package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.object.GameObject;

import java.util.Random;

public class Math {
    public int clampInt(int num, int min, int max) {
        return num < min ? min : (num > max ? max : num);
    }

    public float clampFloat(float num, float min, float max) {
        return num < min ? min : (num > max ? max : num);
    }

    public double clampDouble(double num, double min, double max) {
        return num < min ? min : (num > max ? max : num);
    }

    public int getRandomInt(int min, int max) {
        Random random = new Random();
        return min >= max ? min : random.nextInt(max - min + 1) + min;
    }

    public float getRandomFloat(float min, float max) {
        Random random = new Random();
        return random.nextFloat() * (max - min) + min;
    }

    public float getDistanceTo(GameObject from, GameObject to) {
        return (float) java.lang.Math.sqrt(
                (from.getPositionX() - to.getPositionX()) * (from.getPositionX() - to.getPositionX()) +
                        (from.getPositionY() - to.getPositionY()) * (from.getPositionY() - to.getPositionY())
        );
    }
}