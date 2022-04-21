package de.itg.wahlkampf.object.objects.items;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.effect.effects.RegenerationEffect;
import de.itg.wahlkampf.effect.effects.StrengthEffect;
import de.itg.wahlkampf.object.AbstractItemObject;

import javax.imageio.ImageIO;

public class RegenerationItem extends AbstractItemObject {
    public RegenerationItem(int positionX, int positionY) {
        super("Regeneration Item", positionX, positionY, Game.class.getResource("/de/itg/wahlkampf/assets/items/heart_item.png"));
    }

    @Override
    public void onPickUp() {
        getPlayerObject().addEffects(new RegenerationEffect(5,500, 5000, getPlayerObject()));

    }
}
