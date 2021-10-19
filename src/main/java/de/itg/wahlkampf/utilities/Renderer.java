package de.itg.wahlkampf.utilities;

import java.awt.*;

public class Renderer {
    public void drawCircle(Graphics graphics, int x, int y, int width, int height, Color color) {
        graphics.setColor(color);
        graphics.drawOval(x, y, width, height);
    }

    public void text(Graphics graphics, String string, int x, int y, Color color) {
        graphics.setColor(color);
        graphics.drawString(string, x, y);
    }

    public void drawFillRectangle(Graphics graphics, int x, int y, int width, int height, Color color) {
        graphics.setColor(color);
        graphics.fillRect(x, y, width, height);
    }

    public void drawRectangle(Graphics graphics, int x, int y, int width, int height, Color color) {
        graphics.setColor(color);
        graphics.drawRect(x, y, width, height);
    }

    public void textWithShadow(Graphics graphics, String string, int x, int y, Color color) {
        text(graphics, string, x + 1, y + 1, Color.black);
        text(graphics, string, x, y, color);
    }
}
