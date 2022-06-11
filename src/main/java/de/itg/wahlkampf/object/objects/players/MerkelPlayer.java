package de.itg.wahlkampf.object.objects.players;

import com.sun.tools.javac.Main;
import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.setting.settings.SettingSlider;
import de.itg.wahlkampf.utilities.character.PlayerImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Map;

public class MerkelPlayer extends AbstractPlayerObject {


    private final SettingSlider knockbackModifierHorizontal;
    private final SettingSlider knockbackModifierVertical;

    public MerkelPlayer(int id) {
        super("Merkel", Map.of("stand", new PlayerImage("assets/character/Merkel.png"), "hit", new PlayerImage("assets/character/animation/merkel_hit_edited.png")), id, 100,8, 50, 180, 50, 21, 53);
        knockbackModifierHorizontal = (SettingSlider) Game.instance.getSettingManager().getSettingByName("Knockback Modifier X");
        knockbackModifierVertical = (SettingSlider) Game.instance.getSettingManager().getSettingByName("Knockback Modifier Y");
    }
//214 höhe 86 breite

    @Override
    public void onTick() {
        playerLogic();
    }


    @Override
    public void onAttack(AbstractPlayerObject enemy) {
        if (!enemy.isBlocking() || !enemy.canBlock()) {
            enemy.addDamage(getStrength(), this);
            switch (getAttackFacing()) {
                case RIGHT -> {
                    enemy.setHorizontalMotion((int) (enemy.getHorizontalMotion() + knockbackModifierHorizontal.getCurrentValue()));
                }
                case LEFT -> {
                    enemy.setHorizontalMotion((int) (enemy.getHorizontalMotion() - knockbackModifierHorizontal.getCurrentValue()));
                }
            }
            enemy.setVerticalMotion((int) (enemy.getVerticalMotion() + knockbackModifierVertical.getCurrentValue()));

        } else {
            enemy.setVerticalMotion(enemy.getVerticalMotion() + 5);
        }
    }
}
