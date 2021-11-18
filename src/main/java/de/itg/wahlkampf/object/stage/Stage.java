package de.itg.wahlkampf.object.stage;

public class Stage {
    int x, width, x1,width1;
    int y, height, y1, height1;

    public Stage(int x, int width, int x1, int width1, int y, int height, int y1, int height1) {
        this.x = x;
        this.width = width;
        this.x1 = x1;
        this.width1 = width1;
        this.y = y;
        this.height = height;
        this.y1 = y1;
        this.height1 = height1;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getWidth1() {
        return width1;
    }

    public void setWidth1(int width1) {
        this.width1 = width1;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getHeight1() {
        return height1;
    }

    public void setHeight1(int height1) {
        this.height1 = height1;
    }
}
