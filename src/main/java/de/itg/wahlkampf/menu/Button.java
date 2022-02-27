package de.itg.wahlkampf.menu;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Button {
    private int x, y, width, height;
    private String id, text;
    private boolean visible, enabled;
    private final Renderer renderer;
    private final Font buttonFont;

    public Button(String id, String text, int x, int y, int width, int height, int fontSize, boolean visible, boolean enabled) {
        this.renderer = Game.instance.getRenderer();
        this.id = id;
        this.text = text;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enabled = enabled;
        this.visible = visible;
        buttonFont = new Font("Roboto", Font.BOLD, fontSize);
    }

    public void drawScreen(Graphics graphics, int mouseX, int mouseY) {
        renderer.drawFillRectangle(graphics, x, y, width, height, new Color(255, 255, 255, 100));
        final Rectangle2D fontSize = buttonFont.getStringSize(getText());
        renderer.textWithShadow(graphics, text, x + (width - (int) fontSize.getWidth()) / 2, y + (height + (int) fontSize.getHeight() / 2) / 2, enabled ? new Color(230, 230, 230, 255) : new Color(200, 200, 200, 255), buttonFont);
        if (isHovered(mouseX, mouseY)) {
            renderer.drawRectangle(graphics, x, y, width, height, new Color(255, 255, 255, 200));
        }
    }

    public boolean isHovered(int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public boolean canClick(int mouseX, int mouseY) {
        return isHovered(mouseX, mouseY) && isVisible() && isEnabled();
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

    public String getText() {
        return text;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isEnabled() {
        return enabled;
    }


    public String getId() {
        return id;
    }


    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }
}
