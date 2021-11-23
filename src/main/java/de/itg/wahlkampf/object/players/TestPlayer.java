package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;

import java.awt.*;
import java.awt.event.KeyEvent;

public class TestPlayer extends AbstractPlayerObject {
    public TestPlayer() {
        super("Test Player", 50, 180, 10, 20, 20);
    }

    @Override
    public void onRender(Graphics graphics) {

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
