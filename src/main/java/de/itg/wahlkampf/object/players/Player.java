package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.particlesystem.ParticleType;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends AbstractPlayerObject {

    public Player(int id) {
        super("Player", id, 30, 120, 10, 20, 20);
    }

    @Override
    public void onRender(Graphics graphics) {
        System.out.println(getFacing());
        Wrapper.WRAPPER_INSTANCE.renderer.drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), Color.WHITE);
        Wrapper.WRAPPER_INSTANCE.renderer.drawCircle(graphics, getPositionX(), getEyePosY(), 5, 5, Color.RED);
        Wrapper.WRAPPER_INSTANCE.particleHandler.snakeEffect(getPositionX(), getPositionY(), 1, Color.WHITE, ParticleType.SQUARED, 10, 10, 500);
    }

    @Override
    public void onTick() {
        controlPlayer();
        AbstractPlayerObject enemy = getRayTrace(100);
        if (enemy!= null) {
            System.out.println(enemy.getName());
        }
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void attack(AbstractPlayerObject enemy) {

    }
}
