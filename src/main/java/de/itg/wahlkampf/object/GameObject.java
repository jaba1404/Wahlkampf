package de.itg.wahlkampf.object;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class GameObject {
    private String name;
    private Type type;
    private int positionX, positionY, width, height;

    public GameObject(String name, Type type, int positionX, int positionY, int width, int height) {
        this.name = name;
        this.type = type;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
    }

    public abstract void onRender(Graphics graphics);

    public abstract void onTick();

    public abstract void onKeyPressed(KeyEvent e);

    public abstract void keyReleased(KeyEvent e);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
