package de.itg.wahlkampf.utilities;

public enum Direction {

    LEFT(0, -1),
    RIGHT(0, 1),
    UP(1, 0),
    DOWN(-1, 0),
    ;

    private final int verticalFactor;
    private final int horizontalFactor;

    Direction(int verticalFactor, int horizontalFactor) {
        this.verticalFactor = verticalFactor;
        this.horizontalFactor = horizontalFactor;
    }

    public int getVerticalFactor() {
        return verticalFactor;
    }

    public int getHorizontalFactor() {
        return horizontalFactor;
    }
}
