package de.itg.wahlkampf.object.objects.players;

import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.Direction;
import de.itg.wahlkampf.utilities.character.PlayerImage;

import java.awt.event.KeyEvent;
import java.util.Map;

public class TrumpPlayer extends AbstractPlayerObject {

    public TrumpPlayer(int id) {
        super("Trump", Map.of("stand", new PlayerImage("assets/character/Trump.png"), "hit", new PlayerImage("assets/character/animation/trump_hit_edited.png")), id, 200, 30, 120, 50, 21, 53);
    }


    @Override
    public void onTick() {
        playerLogic();
    }

    @Override
    public void attack(AbstractPlayerObject enemy) {
        if (!enemy.isBlocking() || !enemy.canBlock()) {
            enemy.addDamage(5, this);
            switch (getAttackFacing()) {
                case RIGHT -> {
                    enemy.setFacing(Direction.RIGHT);
                    enemy.setHorizontalMotion(enemy.getHorizontalMotion() + 9);
                }
                case LEFT -> {
                    enemy.setFacing(Direction.LEFT);
                    enemy.setHorizontalMotion(enemy.getHorizontalMotion() - 9);
                }
            }
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 15);

        } else {
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 5);
        }
    }
}
