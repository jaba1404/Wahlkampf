package de.itg.wahlkampf.menu;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;

public class Button {
    private int x,y,width,height;
    private boolean enabled;
    private final Renderer renderer;
    private final Graphics graphics;
    public Button(Graphics graphics,Renderer renderer,int x, int y, int width, int height, boolean enabled) {
        this.graphics = graphics;
        this.renderer = Game.instance.getRenderer();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = enabled;
    }

    protected void drawScreen(int mouseX, int mouseY) {
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
