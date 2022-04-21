package de.itg.wahlkampf.utilities;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;

public class Movement {
    public static final int SPEED = 5;
    public static final int JUMP_MOTION = 8;
    private final MathHelper mathHelper = Game.instance.getMathHelper();

    public void move(AbstractGameObject object, Direction direction) {
        switch (direction) {
            case UP -> object.setPositionY(mathHelper.interpolateValue(object.getPositionY(),object.getPositionY() - SPEED,1));
            case DOWN -> object.setPositionY(mathHelper.interpolateValue(object.getPositionY(),object.getPositionY() + 60, 1));
            case LEFT -> object.setPositionX(mathHelper.interpolateValue(object.getPositionX(),object.getPositionX() - SPEED, 1));
            case RIGHT -> object.setPositionX(mathHelper.interpolateValue(object.getPositionX(),object.getPositionX() + SPEED, 1));
        }
    }

    public void jump(AbstractGameObject object, int height) {
        object.setPositionY(mathHelper.interpolateValue(object.getPositionY(),object.getPositionY() - height,1));
    }

}
