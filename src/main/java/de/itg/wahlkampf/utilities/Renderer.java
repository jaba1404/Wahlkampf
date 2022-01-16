package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.Game;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
    public void img(Graphics g, BufferedImage image, int x, int y) throws IOException {
        //g.drawImage(img, x, y, null);
        g.drawImage(image, x, y, Game.WIDTH, Game.HEIGHT, null);
    }
    public void img(Graphics g, BufferedImage image, int x, int y, int width, int height) {
        //g.drawImage(img, x, y, null);
        g.drawImage(image, x, y, width, height, null);
    }

    public void img2(Graphics g, BufferedImage image, int x, int y) throws IOException {
        //g.drawImage(img, x, y, null);
        g.drawImage(image, x, y, null);
    }
}
