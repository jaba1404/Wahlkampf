package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.awt.event.KeyEvent;

public abstract class AbstractGameObject {
    private String name;
    private Type type;
    private int positionX, positionY, width, height;
    private boolean passThrough;
    private boolean deleted;
    private final Renderer renderer;

    public AbstractGameObject(String name, Type type, int positionX, int positionY, int width, int height, boolean passTrough) {
        this.name = name;
        this.type = type;
        this.positionX = positionX;
        this.positionY = positionY;
        this.width = width;
        this.height = height;
        this.passThrough = passTrough;
        renderer = Game.instance.getRenderer();
    }

    public abstract void onRender(Graphics graphics);

    public abstract void onTick();


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

    public boolean isPassThrough() {
        return passThrough;
    }

    public void setPassThrough(boolean passThrough) {
        this.passThrough = passThrough;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void deleteObject() {
        this.deleted = true;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
