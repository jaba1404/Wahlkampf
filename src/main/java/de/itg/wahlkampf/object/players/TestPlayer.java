package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.Wrapper;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.particlesystem.ParticleType;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class TestPlayer extends AbstractPlayerObject {
    public TestPlayer(int id) {
        super("Test Player", id, 50, 180, 10, 21, 53);
    }
//214 höhe 86 breite
    @Override
    public void onRender(Graphics graphics) {
        try {
            Wrapper.WRAPPER_INSTANCE.renderer.img(graphics, ImageIO.read(new File("resources\\Merkel.png")), getPositionX(), getPositionY(),getWidth(),getHeight());
            Wrapper.WRAPPER_INSTANCE.renderer.drawCircle(graphics, getPositionX(), getEyePosY(), 5,5,Color.RED);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @Override
    public void attack(AbstractPlayerObject enemy) {

    }
}
