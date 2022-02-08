package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;

import java.awt.event.KeyEvent;

public class TrumpPlayer extends AbstractPlayerObject {

    public TrumpPlayer(int id) {
        super("Trump", "resources\\Trump.png", id, 1000, 30, 120, 50, 21, 53);
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
        if (enemy != null)
            enemy.addDamage(5, this);

    }
}