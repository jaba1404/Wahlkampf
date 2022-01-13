package de.itg.wahlkampf.object.objects;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.Type;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class StageBlock extends AbstractGameObject {
    private Color color;
    private BufferedImage[] bufferedImage;
    public StageBlock(int positionX, int positionY, int width, int height, Color color) {
        super("Stage Block", Type.COLLIDABLE, positionX, positionY, width, height);
        this.color = color;
        bufferedImage = null;
    }
    public StageBlock(int positionX, int positionY, int width, int height, BufferedImage[] bufferedImage) {
        super("Stage Block", Type.COLLIDABLE, positionX, positionY, width, height);
        this.bufferedImage = bufferedImage;
        this.color = null;
    }

    @Override
    public void onRender(Graphics graphics) {
        if(bufferedImage == null) {
            Wrapper.WRAPPER_INSTANCE.renderer.drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), color);
        }else {
            try {
                int amount = getWidth() / 30;
                int positionX = getPositionX();
                for(int i = 0; i <= amount; i++) {
                    if(i == 0) {
                        Wrapper.WRAPPER_INSTANCE.renderer.img(graphics, bufferedImage[0], positionX, getPositionY(), 30, 30);
                    }else if(i == amount ) {
                        Wrapper.WRAPPER_INSTANCE.renderer.img(graphics, bufferedImage[2], positionX, getPositionY(), amount, 30);
                    }else {
                        Wrapper.WRAPPER_INSTANCE.renderer.img(graphics, bufferedImage[1], positionX, getPositionY(), 30 , 30);
                    }
                    positionX += 30;
                }
                } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTick() {

    }

    @Override
    public void onKeyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
