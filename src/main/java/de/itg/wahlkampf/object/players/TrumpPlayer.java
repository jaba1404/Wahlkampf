package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;

import java.awt.event.KeyEvent;

public class TrumpPlayer extends AbstractPlayerObject {

    public TrumpPlayer(int id) {
        super("Trump", "Trump.png", id, 200, 30, 120, 50, 21, 53);
    }


    @Override
    public void onTick() {
        playerLogic();
    }

    @Override
    public void onKeyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void attack(AbstractPlayerObject enemy) {
        if (!enemy.isBlocking() || !enemy.canBlock()) {
            enemy.addDamage(5, this);
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 15);
        } else {
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 5);
        }
    }
}
