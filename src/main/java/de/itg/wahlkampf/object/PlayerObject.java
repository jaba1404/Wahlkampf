package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.object.stage.Stage;
import de.itg.wahlkampf.utilities.AnimationUtil;
import de.itg.wahlkampf.utilities.Direction;
import de.itg.wahlkampf.utilities.InputListener;
import de.itg.wahlkampf.utilities.Movement;

import java.awt.event.KeyEvent;


public abstract class PlayerObject extends GameObject {

    private final Movement movement;
    private boolean onGround = false;
    private boolean jump = false;
    private AnimationUtil animationUtil = new AnimationUtil();

    public PlayerObject(String name, int positionX, int positionY, int width, int height) {
        super(name, Type.PLAYER, positionX, positionY, width, height);
        movement = new Movement();
    }

    public void controlPlayer() {
        collide();
        if (!isOnGround()) {
            fall(1.025f);
        }
        if(!InputListener.KEY_LIST.contains(KeyEvent.VK_SPACE))
            jump = true;
        for (Integer integer : InputListener.KEY_LIST) {
            switch (integer) {
                //case KeyEvent.VK_W -> move(Direction.UP);
                case KeyEvent.VK_S -> move(Direction.DOWN);
                case KeyEvent.VK_A -> move(Direction.LEFT);
                case KeyEvent.VK_D -> move(Direction.RIGHT);
                case KeyEvent.VK_SPACE -> {
                    if(jump) {
                        jump(30);
                        jump = false;
                    }
                }
            }
        }
    }
/* need max jump height and animate with animationutil to smoothen it out
 */
    public void collide() {
       /* for (GameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            AxisAligned axisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if (this.getPositionX() > axisAligned.getMinX() && this.getPositionX() + this.getWidth() < axisAligned.getMaxX()) {
                    this.setPositionY((int) axisAligned.getMinY() - gameObject.getHeight());
            } else {
                 fall(1.001f);
            }
        }

        */
        Stage stage = Game.instance.getObjectHandler().stage;
        AxisAligned axisAligned = new AxisAligned(stage.getX(), stage.getX() + stage.getWidth(), stage.getY(), stage.getY() + stage.getHeight());
        AxisAligned axisAligned2 = new AxisAligned(stage.getX1(), stage.getX1() + stage.getWidth1(), stage.getY1(), stage.getY1() + stage.getHeight1());
        if ((this.getPositionX() > axisAligned.getMinX() && this.getPositionX() + this.getWidth() < axisAligned.getMaxX()) || (this.getPositionX() > axisAligned2.getMinX() && this.getPositionX() + this.getWidth() < axisAligned2.getMaxX())) {
            onGround =true;
        } else {
            onGround = false;
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
