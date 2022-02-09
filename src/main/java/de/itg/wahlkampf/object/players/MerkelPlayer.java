package de.itg.wahlkampf.object.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;

import java.awt.event.KeyEvent;

public class MerkelPlayer extends AbstractPlayerObject {
    public MerkelPlayer(int id) {
        super("Merkel", "resources\\Merkel.png", id, 200, 50, 180, 50, 21, 53);
    }
//214 höhe 86 breite

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
