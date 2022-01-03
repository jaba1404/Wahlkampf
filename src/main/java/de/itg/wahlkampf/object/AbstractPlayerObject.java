package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.*;

import java.awt.event.KeyEvent;


public abstract class AbstractPlayerObject extends AbstractGameObject {

    private final Movement movement;
    private final MathHelper mathHelper;
    private boolean onGround;
    private boolean jump;
    private int weight;
    private int damageAmount;
    private int jumpHeight;
    private int id;
    private Direction facing = Direction.RIGHT;
    AnimationUtil animationUtil = new AnimationUtil();
    int test;

    public AbstractPlayerObject(String name, int id, int weight, int positionX, int positionY, int width, int height) {
        super(name, Type.PLAYER, positionX, positionY, width, height);
        this.id = id;
        movement = new Movement();
        mathHelper = new MathHelper();
    }

    public abstract void attack(AbstractPlayerObject enemy);

    public void controlPlayer() {
        collide();
        if (!isOnGround() && jump) {
            fall(1.005f);
        }
        if (!InputListener.KEY_LIST.contains(KeyEvent.VK_SPACE))
            jump = true;
        switch (id) {
            case 0 -> {
                for (Integer integer : InputListener.KEY_LIST) {
                    switch (integer) {
                        case KeyEvent.VK_W -> facing = Direction.UP;
                        case KeyEvent.VK_S -> move(Direction.DOWN);
                        case KeyEvent.VK_A -> move(Direction.LEFT);
                        case KeyEvent.VK_D -> move(Direction.RIGHT);
                        case KeyEvent.VK_SPACE -> {
                            if (jump) {
                                int height = 60;
                                jump((int) animationUtil.getValue(height, 5, 1, 60));
                                if (animationUtil.getValue() >= height) {
                                    jump = false;
                                    animationUtil.reset();
                                }
                                jump = false;
                            }
                        }
                    }
                }
            }
            case 1 -> {
                for (Integer integer : InputListener.KEY_LIST) {
                    switch (integer) {
                        case KeyEvent.VK_UP -> facing = Direction.UP;
                        case KeyEvent.VK_DOWN -> move(Direction.DOWN);
                        case KeyEvent.VK_LEFT -> move(Direction.LEFT);
                        case KeyEvent.VK_RIGHT -> move(Direction.RIGHT);
                        case KeyEvent.VK_SHIFT -> {
                            if (jump) {
                                int height = 60;
                                jump((int) animationUtil.getValue(height, 5, 1, 60));
                                if (animationUtil.getValue() >= height) {
                                    jump = false;
                                    animationUtil.reset();
                                }
                                jump = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public AbstractPlayerObject getRayTrace(int distance) {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.objectHandler.getGameObjects()) {
            if (gameObject instanceof AbstractPlayerObject && gameObject != this) {
                final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
                if (getEyePosY() >= objectsAxisAligned.getMinY() && getEyePosY() <= objectsAxisAligned.getMaxY()) {
                    System.out.println(mathHelper.getDistanceTo(this, gameObject));
                    if (mathHelper.getDistanceTo(this, gameObject) <= distance) {
                        return (AbstractPlayerObject) gameObject;
                    }
                }
            }
        }
        return null;
    }

    /* need max jump height and animate with animationutil to smoothen it out
     */
    public void collide() {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.objectHandler.getGameObjects()) {
            final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if (playerAxisAligned.getMaxX() > objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() < objectsAxisAligned.getMaxX()) {
                if (playerAxisAligned.getMaxY() >= objectsAxisAligned.getMinY() - 1 && (playerAxisAligned.getMinY() + (this.getHeight() / 2)) < (objectsAxisAligned.getMinY() + objectsAxisAligned.getMaxY()) / 2) {
                    if (this.getPositionY() + this.getHeight() != objectsAxisAligned.getMinY() - 1) {
                        setPositionY(objectsAxisAligned.getMinY() - this.getHeight() - 1);
                    }
                    onGround = true;
                    break;
                }
            }
        }
    }

    public int getEyePosY() {
        return getPositionY() + (getHeight() / 6);
    }

    public void move(Direction direction) {
        movement.move(this, direction);
        this.facing = direction;
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

    public void addDamage(int damage) {
        damageAmount = damageAmount + damage;
    }

    public int getDamageAmount() {
        return damageAmount;
    }

    public void setDamageAmount(int damageAmount) {
        this.damageAmount = damageAmount;
    }

    public Direction getFacing() {
        return this.facing;
    }

    //https://www.ssbwiki.com/Knockback#Formula
    public int calculateKnockBack(int percentage, int damage, int weight, int knockBackScaling, int baseKnockBack,
                                  int ratio) {
        return (int) (((((((percentage / 10) + (percentage * damage) / 20) * (200 / (weight + 100)) * 1.4) + 18) * knockBackScaling) + baseKnockBack) * ratio);
    }
}
