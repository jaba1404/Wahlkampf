package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.PlayerObject;
import de.itg.wahlkampf.utilities.particlesystem.ParticleType;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends PlayerObject {
    public Player() {
        super("Player", 120, 10, 20, 20);
    }

    @Override
    public void onRender(Graphics graphics) {
        Wrapper.WRAPPER_INSTANCE.renderer.drawFillRectangle(graphics, getPositionX(), getPositionY(), getWidth(), getHeight(), Color.WHITE);
        Wrapper.WRAPPER_INSTANCE.particleHandler.snakeEffect(getPositionX(), getPositionY(), 1, Color.WHITE, ParticleType.SQUARED, 10, 10, 500);
    }

    @Override
    public void onTick() {
        controlPlayer();
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}
