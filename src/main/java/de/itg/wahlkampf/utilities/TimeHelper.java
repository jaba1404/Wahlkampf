package de.itg.wahlkampf.utilities;

public class TimeHelper {
    private long lastMS;

    private long getCurrentMS() {
        return System.currentTimeMillis();
    }

    private long getDifference() {
        return getCurrentMS() - lastMS;
    }

    public boolean hasPassed(long l) {
        return getDifference() >= l;
    }

    public void reset() {
        lastMS = getCurrentMS();
    }
}
