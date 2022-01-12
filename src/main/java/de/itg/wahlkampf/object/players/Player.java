package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.particlesystem.ParticleType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

public class Player extends AbstractPlayerObject {

    public Player(int id) {
        super("Player", id, 30, 120, 10, 21, 53);
    }

    @Override
    public void onRender(Graphics graphics) {
        System.out.println(getFacing());
        try {
            Wrapper.WRAPPER_INSTANCE.renderer.img(graphics, ImageIO.read(new File("resources\\Trump.png")), getPositionX(), getPositionY(),getWidth(),getHeight());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
