package de.itg.wahlkampf.object.objects;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.Type;

import java.awt.*;
import java.awt.event.KeyEvent;

public class StageBlock extends AbstractGameObject {
    public StageBlock(int positionX, int positionY, int width, int height) {
        super("Stage Block", Type.COLLIDABLE, positionX, positionY, width, height);
    }

    @Override
    public void onRender(Graphics graphics) {
        Wrapper.WRAPPER_INSTANCE.renderer.drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), Color.WHITE);
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
