package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.Direction;
import de.itg.wahlkampf.utilities.InputListener;
import de.itg.wahlkampf.utilities.Movement;

import java.awt.event.KeyEvent;


public abstract class PlayerObject extends GameObject {

    private final Movement movement;
    private boolean onGround = false;

    public PlayerObject(String name, int positionX, int positionY, int width, int height) {
        super(name, Type.PLAYER, positionX, positionY, width, height);
        movement = new Movement();
    }

    public void controlPlayer() {
        collide();
        if (!isOnGround()) {
            //fall(1.04f);
        }
        for (Integer integer : InputListener.KEY_LIST) {
            switch (integer) {
                case KeyEvent.VK_W -> move(Direction.UP);
                case KeyEvent.VK_S -> move(Direction.DOWN);
                case KeyEvent.VK_A -> move(Direction.LEFT);
                case KeyEvent.VK_D -> move(Direction.RIGHT);
                case KeyEvent.VK_SPACE -> jump(15);
            }
        }
    }

    public void collide() {
        for (GameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            AxisAligned axisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if (this.getPositionX() > axisAligned.getMinX() && this.getPositionX() + this.getWidth() < axisAligned.getMaxX()) {
                //    this.setPositionY((int) axisAligned.getMinY() - gameObject.getHeight());
            } else {
                //  fall(1.001f);
            }
        }
    }

    public void move(Direction direction) {
        movement.move(this, direction);
    }

    public void jump(int height) {
        movement.jump(this, height);
    }

    public void fall(float multiplier) {
        movement.fall(this, multiplier);
    }

    public boolean isOnGround() {
        return onGround;
    }
}
