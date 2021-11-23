package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.object.AbstractGameObject;

public class Collision {
    //wip
    public static boolean inVoid(AbstractGameObject gameObject, int startX, int endX, int startY, int endY) {
        if (gameObject.getPositionX() >= startX && gameObject.getPositionX() + gameObject.getWidth() <= endX && gameObject.getPositionY() >= endY && gameObject.getPositionY() <= startY) {
            return true;
        }
        return false;
    }
}
