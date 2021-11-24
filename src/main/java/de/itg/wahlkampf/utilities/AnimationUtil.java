package de.itg.wahlkampf.utilities;

public class AnimationUtil {

    private double value;
    private double initValue;
    private final TimeHelper time = new TimeHelper();

    public AnimationUtil() {
    }

    public AnimationUtil(double value) {
        initValue = this.value = value;
    }

    public void reset() {
        value = initValue;
    }

    public double getValue(double should, double slowdown, double diff, int FPS) {
        if (time.hasPassed(1000 / FPS)) {
            double difference = Math.abs(value - should);
            if (difference > diff) {
                if (value < should) {
                    value += (should - value) / slowdown;
                }
                if (value > should) {
                    value -= (value - should) / slowdown;
                }
            } else {
                value = should;
            }
            time.reset();
        }
        return value;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}