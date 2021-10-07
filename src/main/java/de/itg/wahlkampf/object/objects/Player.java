package de.itg.wahlkampf.object.objects;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.GameObject;
import de.itg.wahlkampf.object.Type;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends GameObject {
    public Player() {
        super("Player", Type.PLAYER, 120, 10);
    }

    @Override
    public void onRender(Graphics graphics) {
        Wrapper.WRAPPER_INSTANCE.renderer.drawCircle(graphics,10,10,20,20,Color.BLACK);
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
