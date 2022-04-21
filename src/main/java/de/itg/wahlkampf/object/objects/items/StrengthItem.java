package de.itg.wahlkampf.object.objects.items;
import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.effect.effects.RegenerationEffect;
import de.itg.wahlkampf.effect.effects.StrengthEffect;
import de.itg.wahlkampf.object.AbstractItemObject;


public class StrengthItem extends AbstractItemObject {


    public StrengthItem(int positionX, int positionY) {
        super("Strength Item", positionX, positionY, Game.class.getResource("/de/itg/wahlkampf/assets/items/strength_item.png"));
    }

    @Override
    public void onPickUp() {
        getPlayerObject().addEffects(new StrengthEffect(25,10000, getPlayerObject()));
    }
}
