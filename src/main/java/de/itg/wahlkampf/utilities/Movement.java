package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.object.GameObject;

public class Movement {
    public static final int SPEED = 5;

    public void move(GameObject object, Direction direction) {
        switch (direction) {
            case UP -> object.setPositionY(object.getPositionY() - SPEED);
            case DOWN -> object.setPositionY(object.getPositionY() + SPEED);
            case LEFT -> object.setPositionX(object.getPositionX() - SPEED);
            case RIGHT -> object.setPositionX(object.getPositionX() + SPEED);
        }
    }

    public void jump(GameObject object, int height) {
        object.setPositionY(object.getPositionY() - height);
    }

    public void fall(GameObject gameObject, float multiplier) {
        gameObject.setPositionY((int) ((gameObject.getPositionY() + SPEED) * multiplier));
    }
}
