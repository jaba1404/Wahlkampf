package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public abstract class AbstractPlayerObject extends AbstractGameObject {

    private final Movement movement;
    private final MathHelper mathHelper;
    private boolean onGround;
    private boolean jump;
    private int weight;
    private String path;
    private int damageAmount;
    private int jumpHeight;
    private MusicHelper musicHelper;
    private int id;
    private Direction facing = Direction.RIGHT;
    private BufferedImage bufferedImage;
    private BufferedImage bufferedImageFlipH;
    private BufferedImage bufferedImageFlipV;
    private final Renderer renderer;

    private static final int FLIP_VERTICAL = 1;
    private static final int FLIP_HORIZONTAL = -1;

    public AbstractPlayerObject(String name, String path, int id, int weight, int positionX, int positionY, int width, int height) {
        super(name, Type.PLAYER, positionX, positionY, width, height, true);
        this.id = id;
        this.path = path;
        renderer = Game.instance.getRenderer();
        movement = new Movement();
        mathHelper = new MathHelper();
        musicHelper = new MusicHelper();
        try {
            bufferedImage = ImageIO.read(new File(path));
            bufferedImageFlipH = flip(bufferedImage, FLIP_HORIZONTAL);
            bufferedImageFlipV = flip(bufferedImage, FLIP_VERTICAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public abstract void attack(AbstractPlayerObject enemy);

    @Override
    public void onRender(Graphics graphics) {
        final BufferedImage test = facing == Direction.RIGHT ? bufferedImage : bufferedImageFlipH;
        renderer.img(graphics, test, getPositionX(), getPositionY(), getWidth(), getHeight());
        renderer.drawCircle(graphics, getPositionX(), getEyePosY(), 5, 5, Color.RED);
    }

    public void controlPlayer() {
        collide();
        if (!isOnGround() && jump) {
            fall(1.005f);
        }
        if (!InputListener.KEY_LIST.contains(KeyEvent.VK_SPACE) && !InputListener.KEY_LIST.contains(KeyEvent.VK_SHIFT))
            jump = true;
        switch (id) {
            case 0 -> {
                for (Integer integer : InputListener.KEY_LIST) {
                    switch (integer) {
                        //case KeyEvent.VK_W -> facing = Direction.UP;
                        case KeyEvent.VK_S -> {
                            final AbstractGameObject gameObject = getObjectStandingOn();
                            if (gameObject != null && gameObject.isPassThrough()) {
                                setPositionY(getPositionY() + getHeight() / 2 + getObjectStandingOn().getHeight());
                            }
                        }
                        case KeyEvent.VK_A -> move(Direction.LEFT);
                        case KeyEvent.VK_D -> move(Direction.RIGHT);
                        case KeyEvent.VK_SPACE -> {
                            if (jump) {
                                int height = 60;
                                jump(60);
                                jump = false;
                            }
                        }
                    }
                }
            }
            case 1 -> {
                for (Integer integer : InputListener.KEY_LIST) {
                    switch (integer) {
                        //case KeyEvent.VK_UP -> facing = Direction.UP;
                        case KeyEvent.VK_DOWN -> {
                            final AbstractGameObject gameObject = getObjectStandingOn();
                            if (gameObject != null && gameObject.isPassThrough()) {
                                setPositionY(getPositionY() + getHeight() / 2 + getObjectStandingOn().getHeight());
                            }
                        }
                        case KeyEvent.VK_LEFT -> move(Direction.LEFT);
                        case KeyEvent.VK_RIGHT -> move(Direction.RIGHT);
                        case KeyEvent.VK_SHIFT -> {
                            if (jump) {
                                jump(60);
                                jump = false;
                            }
                        }
                    }
                }
            }
        }
    }

    public AbstractPlayerObject getRayTrace(int distance, Direction direction) {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            if (gameObject instanceof AbstractPlayerObject && gameObject != this) {
                final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
                if (getEyePosY() >= objectsAxisAligned.getMinY() && getEyePosY() <= objectsAxisAligned.getMaxY()) {
                    if (mathHelper.getDistanceTo(this, gameObject) <= distance) {
                        return (AbstractPlayerObject) gameObject;
                    }
                }
            }
        }
        return null;
    }

    public AbstractGameObject getObjectStandingOn() {
        if (!onGround)
            return null;
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        final int midPoint = playerAxisAligned.getMinX() + playerAxisAligned.getMaxX() / 2;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
            if (playerAxisAligned.getMaxY() == objectsAxisAligned.getMinY() - 1 && playerAxisAligned.getMaxX() > objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() < objectsAxisAligned.getMaxX()) {
                return gameObject;
            }
        }
        return null;
    }

    public void collide() {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
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
        if (direction != Direction.UP && direction != Direction.DOWN) {
            this.facing = direction;
        }
    }

    private BufferedImage flip(BufferedImage image, int direction) {
        int width = image.getWidth();
        int height = image.getHeight();
        final BufferedImage flipped = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                switch (direction) {
                    case FLIP_HORIZONTAL -> flipped.setRGB((width - 1) - x, y, image.getRGB(x, y));
                    case FLIP_VERTICAL -> flipped.setRGB(x, (height - 1) - y, image.getRGB(x, y));
                }
            }
        }
        return flipped;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    //https://www.ssbwiki.com/Knockback#Formula
    public int calculateKnockBack(int percentage, int damage, int weight, int knockBackScaling, int baseKnockBack,
                                  int ratio) {
        return (int) (((((((percentage / 10) + (percentage * damage) / 20) * (200 / (weight + 100)) * 1.4) + 18) * knockBackScaling) + baseKnockBack) * ratio);
    }
}
