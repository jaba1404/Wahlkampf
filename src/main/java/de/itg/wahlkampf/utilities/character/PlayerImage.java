package de.itg.wahlkampf.utilities.character;

import de.itg.wahlkampf.Game;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class PlayerImage {
    private BufferedImage bufferedImage;
    private final BufferedImage flippedHorizontal;
    private final BufferedImage flippedVertical;

    public PlayerImage(String path) {
        try {
            bufferedImage = ImageIO.read(Game.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        flippedHorizontal = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        flippedVertical = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                flippedHorizontal.setRGB((width - 1) - x, y, bufferedImage.getRGB(x, y));
                flippedVertical.setRGB(x, (height - 1) - y, bufferedImage.getRGB(x, y));
            }
        }
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public BufferedImage getBufferedImageFlippedHorizontal() {
        return flippedHorizontal;
    }

    public BufferedImage getBufferedImageFlippedVertical() {
        return flippedVertical;
    }
}
