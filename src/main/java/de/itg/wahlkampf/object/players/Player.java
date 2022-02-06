package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;

import java.awt.event.KeyEvent;

public class Player extends AbstractPlayerObject {

    public Player(int id) {
        super("Player","resources\\Trump.png", id, 30, 120, 10, 21, 53);
    }


    @Override
    public void onTick() {
        controlPlayer();
        AbstractPlayerObject enemy = getRayTrace(100, getFacing());
        if (enemy != null) {

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
