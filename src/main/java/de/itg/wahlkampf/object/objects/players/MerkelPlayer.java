package de.itg.wahlkampf.object.objects.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.character.PlayerImage;

import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map;

public class MerkelPlayer extends AbstractPlayerObject {
    public MerkelPlayer(int id) {

        super("Merkel", Map.of("stand", new PlayerImage("assets/character/Merkel.png"), "hit", new PlayerImage("assets/character/animation/merkel_hit_edited.png")), id, 200, 50, 180, 50, 21, 53);
    }
//214 höhe 86 breite

    @Override
    public void onTick() {
        playerLogic();
    }

    @Override
    public void attack(AbstractPlayerObject enemy) {
        if (!enemy.isBlocking()) {
            enemy.addDamage(5, this);
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 15);
        } else {
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 5);
        }
    }
}
