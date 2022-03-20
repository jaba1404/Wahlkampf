package de.itg.wahlkampf.utilities;

public enum Direction {

    LEFT(0, -1),
    RIGHT(0, 1),
    UP(1, 0),
    DOWN(-1, 0),
    ;

    private int verticalFactor;
    private int horizontalFactor;

    Direction(int verticalFactor, int horizontalFactor) {
        this.verticalFactor = verticalFactor;
        this.horizontalFactor = horizontalFactor;
    }

    public int getVerticalFactor() {
        return verticalFactor;
    }

    public void setVerticalFactor(int verticalFactor) {
        this.verticalFactor = verticalFactor;
    }

    public void setHorizontalFactor(int horizontalFactor) {
        this.horizontalFactor = horizontalFactor;
    }

    public int getHorizontalFactor() {
        return horizontalFactor;
    }
}
