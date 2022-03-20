package de.itg.wahlkampf.object.objects.items;
import de.itg.wahlkampf.object.AbstractItemObject;


public class StrengthItem extends AbstractItemObject {


    public StrengthItem(int positionX, int positionY) {
        super("Strength Item", positionX, positionY);
    }

    @Override
    public void onPickUp() {
        getPlayerObject().setHealthPoints(100000);
    }
}
