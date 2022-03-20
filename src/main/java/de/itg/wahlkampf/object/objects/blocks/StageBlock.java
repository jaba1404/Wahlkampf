package de.itg.wahlkampf.object.objects.blocks;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.Type;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class StageBlock extends AbstractGameObject {
    private final Color color;
    private final BufferedImage[] bufferedImage;

    public StageBlock(int positionX, int positionY, int width, int height, Color color, boolean passThrough) {
        super("Stage Block", Type.COLLIDABLE, positionX, positionY, width, height, passThrough);
        this.color = color;
        bufferedImage = null;
    }

    public StageBlock(int positionX, int positionY, int width, int height, BufferedImage[] bufferedImage, boolean passThrough) {
        super("Stage Block", Type.COLLIDABLE, positionX, positionY, width, height, passThrough);
        this.bufferedImage = bufferedImage;
        this.color = null;
    }

    @Override
    public void onRender(Graphics graphics) {
        if (bufferedImage == null) {
            getRenderer().drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), color);
        } else {
            final int amount = getWidth() / 30;
            int positionX = getPositionX();
            for (int i = 0; i < amount; i++) {
                if (i == 0) {
                    getRenderer().img(graphics, bufferedImage[0], positionX, getPositionY(), 30, getHeight());
                } else if (i == amount - 1) {
                    getRenderer().img(graphics, bufferedImage[2], positionX, getPositionY(), 30, getHeight());
                } else {
                    getRenderer().img(graphics, bufferedImage[1], positionX, getPositionY(), 30, getHeight());
                }
                positionX += 30;
            }
        }
    }

    @Override
    public void onTick() {

    }
}
