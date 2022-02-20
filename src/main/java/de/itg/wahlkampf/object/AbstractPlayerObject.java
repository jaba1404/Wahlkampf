package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.GameFinishedEvent;
import de.itg.wahlkampf.event.impl.PlayerAttackEvent;
import de.itg.wahlkampf.event.impl.PlayerBlockEvent;
import de.itg.wahlkampf.event.impl.PlayerJumpEvent;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.*;
import de.itg.wahlkampf.utilities.sound.SoundHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public abstract class AbstractPlayerObject extends AbstractGameObject {

    private static final int INITIAL_LIVES = 3;
    private static final int INITIAL_BLOCKS = 3;
    private static final int MAX_AIR_JUMPS = 3;
    private static final long BLOCK_DELAY = 2000;
    private static final int FLIP_VERTICAL = 1;
    private static final int FLIP_HORIZONTAL = -1;

    private final Movement movement;
    private final MathHelper mathHelper;
    private final PlayerJumpEvent playerJumpEvent;
    private final PlayerAttackEvent playerAttackEvent;

    private final TimeHelper timeHelper;
    private boolean onGround;
    private boolean jump;
    private boolean attack;
    private boolean clip;
    private boolean blocking;
    private int weight;
    private String path;
    private int healthPoints;
    private int lives = INITIAL_LIVES;
    private int blockCounter;
    private final SoundHelper soundHelper;
    private final int id;
    private Direction facing = Direction.RIGHT;
    private Direction attackFacing = Direction.RIGHT;
    private BufferedImage bufferedImage;
    private BufferedImage bufferedImageFlipH;
    private BufferedImage bufferedImageFlipV;
    private final Renderer renderer;
    private AbstractPlayerObject lastDamageSource;
    private final int spawnX, spawnY, spawnHealth;
    private int verticalMotion;
    private int horizontalMotion;
    private int jumpCounter;

    private final Map<Action, Integer> keyBindMap;

    public AbstractPlayerObject(String name, String path, int id, int healthPoints, int weight, int positionX, int positionY, int width, int height) {
        super(name, Type.PLAYER, positionX, positionY, width, height, true);
        this.timeHelper = new TimeHelper();
        this.keyBindMap = new HashMap<>();
        this.id = id;
        this.path = path;
        this.healthPoints = healthPoints;
        this.renderer = Game.instance.getRenderer();
        this.movement = new Movement();
        this.mathHelper = new MathHelper();
        this.soundHelper = new SoundHelper();
        this.spawnX = positionX;
        this.spawnY = positionY;
        this.spawnHealth = healthPoints;
        try {
            this.bufferedImage = ImageIO.read(Game.class.getResource("assets/" + path));
            this.bufferedImageFlipH = flip(bufferedImage, FLIP_HORIZONTAL);
            this.bufferedImageFlipV = flip(bufferedImage, FLIP_VERTICAL);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        playerAttackEvent = new PlayerAttackEvent(this);
        playerJumpEvent = new PlayerJumpEvent(this);
        addKeyBinds();
    }

    public abstract void attack(AbstractPlayerObject enemy);

    @Override
    public void onRender(Graphics graphics) {
        final BufferedImage player = facing == Direction.RIGHT ? bufferedImage : bufferedImageFlipH;
        renderer.img(graphics, player, getPositionX(), getPositionY(), getWidth(), getHeight());
        renderer.drawCircle(graphics, getPositionX(), getEyePosY(), 5, 5, Color.RED);
        if (isBlocking() && canBlock()) {
            renderer.drawFillCircle(graphics, getPositionX() - 5, getPositionY() - 5, getWidth() + 10, getHeight() + 10, new Color(118, 231, 118, 150));
        }
        if(facing.getHorizontalFactor() != 0) {
            final AbstractGameObject below = getObjectStandingOn();
            if(below != null) {
            }
        }
    }

    public void playerLogic() {
        if (getPositionY() > 0) {
            setPositionY(getPositionY() - verticalMotion);
        }
        setPositionX(getPositionX() + horizontalMotion);

        if (!isOnGround()) {
            if (getPositionY() > 0) {
                verticalMotion *= 0.91;
                verticalMotion -= 2;
            } else {
                verticalMotion = 0;
                setPositionY(1);
            }
        } else if (verticalMotion < 0) {
            verticalMotion = 0;
        }

        if (Math.abs(horizontalMotion) >= 1) {
            horizontalMotion -= facing.getHorizontalFactor();
        }

        collide();


        if (isOnGround()) {
            jumpCounter = 0;
        }

        if (!InputListener.KEY_LIST.contains(keyBindMap.get(Action.ATTACK))) {
            attack = true;
        }

        if (!InputListener.KEY_LIST.contains(keyBindMap.get(Action.CLIP))) {
            clip = true;
        }

        if (!InputListener.KEY_LIST.contains(keyBindMap.get(Action.JUMP))) {
            jump = true;
        }
        setBlocking(false);

        final List<Integer> keyList = new ArrayList<>(InputListener.KEY_LIST);
        for (Integer integer : keyList) {
            if (Objects.equals(integer, keyBindMap.get(Action.CLIP))) {
                if (clip) {
                    final AbstractGameObject gameObject = getObjectStandingOn();
                    if (gameObject != null && gameObject.isPassThrough()) {
                        setPositionY(getPositionY() + 2 + getHeight() / 2 + getObjectStandingOn().getHeight() / 2);
                    }
                    clip = false;
                }
            } else if (Objects.equals(integer, keyBindMap.get(Action.ATTACK))) {
                if (attack) {
                    final AbstractPlayerObject rayTraced = getRayTrace(attackFacing == Direction.DOWN || attackFacing == Direction.UP ? 60 : 40, attackFacing);
                    if (rayTraced != null) {
                        attack(rayTraced);
                        if (rayTraced.isBlocking() && rayTraced.canBlock()) {
                            final PlayerBlockEvent playerBlockEvent = new PlayerBlockEvent(this, rayTraced);
                            Game.instance.onEvent(playerBlockEvent);
                            rayTraced.setBlockCounter(rayTraced.getBlockCounter() + 1);
                        } else {
                            Game.instance.onEvent(playerAttackEvent);
                        }
                    }
                    attack = false;
                }
            } else if (Objects.equals(integer, keyBindMap.get(Action.BLOCK))) {
                setBlocking(true);
                setHorizontalMotion(getHorizontalMotion() / 2);
            } else if (Objects.equals(integer, keyBindMap.get(Action.FORWARDS))) {
                setAttackFacing(Direction.RIGHT);
                move(Direction.RIGHT);
            } else if (Objects.equals(integer, keyBindMap.get(Action.BACKWARDS))) {
                setAttackFacing(Direction.LEFT);
                move(Direction.LEFT);
            } else if (Objects.equals(integer, keyBindMap.get(Action.UP))) {
                setAttackFacing(Direction.UP);
            } else if (Objects.equals(integer, keyBindMap.get(Action.DOWN))) {
                setAttackFacing(Direction.DOWN);
            } else if (Objects.equals(integer, keyBindMap.get(Action.JUMP))) {
                if (jump && ++jumpCounter <= MAX_AIR_JUMPS) {
                    verticalMotion = 30;
                    jump = false;
                    horizontalMotion += 2 * facing.getHorizontalFactor();
                    Game.instance.onEvent(playerJumpEvent);
                }
            }
        }
        if (!canBlock() || !isBlocking()) {
            if (timeHelper.hasPassed(BLOCK_DELAY)) {
                blockCounter = 0;
                timeHelper.reset();
            }
        } else {
            timeHelper.reset();
        }
        if (healthPoints <= 0 && lives > 0) {
            respawnPlayer();
        }
        if (lives <= 0) {
            final GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(lastDamageSource);
            Game.instance.onEvent(gameFinishedEvent);
        }
    }

    private void respawnPlayer() {
        lives--;
        healthPoints = spawnHealth;
        setPositionX(spawnX);
        setPositionY(spawnY);
    }

    public AbstractPlayerObject getRayTrace(int distance, Direction direction) {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            if (gameObject instanceof AbstractPlayerObject && gameObject != this) {
                System.out.println(mathHelper.getDistanceTo(this, gameObject));
                final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
                System.out.println(direction);
                switch (direction) {
                    case DOWN, UP -> {
                        if (playerAxisAligned.getMaxX() >= objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() <= objectsAxisAligned.getMaxX()) {
                            System.out.println("wdfadaw");
                            if (mathHelper.getDistanceTo(this, gameObject) <= distance) {
                                return (AbstractPlayerObject) gameObject;
                            }
                        }
                    }
                    case RIGHT -> {
                        if (getEyePosY() >= objectsAxisAligned.getMinY() && getEyePosY() <= objectsAxisAligned.getMaxY()) {
                            if (playerAxisAligned.getMaxX() >= objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() + getWidth() / 2 <= objectsAxisAligned.getMaxX()) {
                                System.out.println("dfawfawafafaw");
                                return (AbstractPlayerObject) gameObject;
                            } else if (playerAxisAligned.getMaxX() <= objectsAxisAligned.getMinX()) {
                                if (mathHelper.getDistanceTo(this, gameObject) <= distance) {
                                    return (AbstractPlayerObject) gameObject;
                                }
                            }
                        }
                    }
                    case LEFT -> {
                        if (getEyePosY() >= objectsAxisAligned.getMinY() && getEyePosY() <= objectsAxisAligned.getMaxY()) {
                            if (playerAxisAligned.getMinX() <= objectsAxisAligned.getMaxX() && playerAxisAligned.getMaxX() - getWidth() / 2 >= objectsAxisAligned.getMinX()) {
                                System.out.println("dfawfawafafaw");

                                return (AbstractPlayerObject) gameObject;
                            } else if (playerAxisAligned.getMinX() >= objectsAxisAligned.getMaxX()) {
                                if (mathHelper.getDistanceTo(this, gameObject) <= distance) {
                                    return (AbstractPlayerObject) gameObject;
                                }
                            }
                        }
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
        //movement.move(this, direction);
        if (direction.getHorizontalFactor() != 0) {
            this.facing = direction;
        }

        horizontalMotion += 2 * facing.getHorizontalFactor();
        horizontalMotion *= 1.2;
        horizontalMotion = Math.min(5, horizontalMotion);
        horizontalMotion = Math.max(-5, horizontalMotion);
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

    private void addKeyBinds() {
        switch (this.getId()) {
            case 0 -> {
                keyBindMap.put(Action.FORWARDS, KeyEvent.VK_D);
                keyBindMap.put(Action.BACKWARDS, KeyEvent.VK_A);
                keyBindMap.put(Action.JUMP, KeyEvent.VK_SPACE);
                keyBindMap.put(Action.CLIP, KeyEvent.VK_Y);
                keyBindMap.put(Action.ATTACK, KeyEvent.VK_X);
                keyBindMap.put(Action.BLOCK, KeyEvent.VK_C);
                keyBindMap.put(Action.DOWN, KeyEvent.VK_S);
                keyBindMap.put(Action.UP, KeyEvent.VK_W);
            }
            case 1 -> {
                keyBindMap.put(Action.FORWARDS, KeyEvent.VK_RIGHT);
                keyBindMap.put(Action.BACKWARDS, KeyEvent.VK_LEFT);
                keyBindMap.put(Action.JUMP, KeyEvent.VK_SHIFT);
                keyBindMap.put(Action.CLIP, KeyEvent.VK_DOWN);
                keyBindMap.put(Action.ATTACK, KeyEvent.VK_CONTROL);
                keyBindMap.put(Action.BLOCK, KeyEvent.VK_NUMPAD0);
                keyBindMap.put(Action.DOWN, KeyEvent.VK_DOWN);
                keyBindMap.put(Action.UP, KeyEvent.VK_UP);
            }
        }
    }

    public void jump(int height) {
        movement.jump(this, height);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void addDamage(int damage, AbstractPlayerObject damageSource) {
        healthPoints = healthPoints - damage;
        lastDamageSource = damageSource;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int damageAmount) {
        this.healthPoints = damageAmount;
    }

    public Direction getFacing() {
        return this.facing;
    }

    public void setFacing(Direction facing) {
        this.facing = facing;
    }

    public Direction getAttackFacing() {
        return attackFacing;
    }

    public void setAttackFacing(Direction attackFacing) {
        this.attackFacing = attackFacing;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public SoundHelper getSoundHelper() {
        return soundHelper;
    }

    public boolean canBlock() {
        return this.getBlockCounter() < INITIAL_BLOCKS;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    //https://www.ssbwiki.com/Knockback#Formula
    public int calculateKnockBack(int percentage, int damage, int weight, int knockBackScaling, int baseKnockBack,
                                  int ratio) {
        return (int) (((((((percentage / 10) + (percentage * damage) / 20) * (200 / (weight + 100)) * 1.4) + 18) * knockBackScaling) + baseKnockBack) * ratio);
    }

    public void setHorizontalMotion(int horizontalMotion) {
        this.horizontalMotion = horizontalMotion;
    }

    public int getHorizontalMotion() {
        return horizontalMotion;
    }

    public int getVerticalMotion() {
        return verticalMotion;
    }

    public void setVerticalMotion(int verticalMotion) {
        this.verticalMotion = verticalMotion;
    }

    public Map<Action, Integer> getKeyBindMap() {
        return keyBindMap;
    }

    public int getId() {
        return id;
    }

    public boolean isBlocking() {
        return blocking;
    }

    public int getBlockCounter() {
        return blockCounter;
    }

    public void setBlockCounter(int blockCounter) {
        this.blockCounter = blockCounter;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
    }
}

