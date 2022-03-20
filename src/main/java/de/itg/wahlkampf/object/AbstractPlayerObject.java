package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.GameFinishedEvent;
import de.itg.wahlkampf.event.impl.PlayerAttackEvent;
import de.itg.wahlkampf.event.impl.PlayerBlockEvent;
import de.itg.wahlkampf.event.impl.PlayerJumpEvent;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.*;
import de.itg.wahlkampf.utilities.character.PlayerImage;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;


public abstract class AbstractPlayerObject extends AbstractGameObject {

    private static final int INITIAL_LIVES = 3;
    private static final int INITIAL_BLOCKS = 3;
    private static final int MAX_AIR_JUMPS = 3;
    private static final long BLOCK_DELAY = 2000;

    private final Movement movement;
    private final MathHelper mathHelper;
    private final PlayerJumpEvent playerJumpEvent;
    private final PlayerAttackEvent playerAttackEvent;
    private final TimeHelper timeHelper;
    private final TimeHelper attackTimeHelper;

    private boolean onGround;
    private boolean canJump;
    private boolean canAttack;
    private boolean canClip;
    private boolean attacking;
    private boolean blocking;
    private int weight;
    private Map<String, PlayerImage> imageMap;
    private int healthPoints;
    private int lives = INITIAL_LIVES;
    private int blockCounter;
    private final int id;
    private Direction facing = Direction.RIGHT;
    private Direction attackFacing = Direction.RIGHT;
    private AbstractPlayerObject lastDamageSource;
    private final int spawnHealth, spawnY;
    private int verticalMotion;
    private int horizontalMotion;
    private int jumpCounter;

    private final int difference;

    private final Map<Action, KeyBind> keyBindMap;

    public AbstractPlayerObject(String name, Map<String, PlayerImage> imageMap, int id, int healthPoints, int weight, int positionX, int positionY, int width, int height) {
        super(name, Type.COLLIDABLE, positionX, positionY, width, height, true);
        this.timeHelper = new TimeHelper();
        this.attackTimeHelper = new TimeHelper();
        this.keyBindMap = new HashMap<>();
        this.imageMap = imageMap;
        this.id = id;
        this.healthPoints = healthPoints;
        this.movement = new Movement();
        this.mathHelper = new MathHelper();
        this.spawnHealth = healthPoints;
        this.spawnY = positionY;
        playerAttackEvent = new PlayerAttackEvent(this);
        playerJumpEvent = new PlayerJumpEvent(this);
        addKeyBinds();

        difference = Math.abs(imageMap.get("hit").getBufferedImage().getWidth() - imageMap.get("stand").getBufferedImage().getWidth());
    }

    public abstract void attack(AbstractPlayerObject enemy);

    @Override
    public void onRender(Graphics graphics) {
        final PlayerImage playerImage = attacking ? imageMap.get("hit") : imageMap.get("stand");
        final BufferedImage playerImageAdjusted = facing == Direction.RIGHT ? playerImage.getBufferedImage() : playerImage.getBufferedImageFlippedHorizontal();
        getRenderer().img(graphics, playerImageAdjusted, attacking ? getFacing() == Direction.LEFT ? (getPositionX() - difference / 4) - 5 : getPositionX() + 5 : getPositionX(), getPositionY(), attacking ? getWidth() + difference / 4 : getWidth(), getHeight());
        if (isBlocking() && canBlock()) {
            getRenderer().drawFillCircle(graphics, getPositionX() - 5, getPositionY() - 5, getWidth() + 10, getHeight() + 10, new Color(118, 231, 118, 150));
        }
        if (facing.getHorizontalFactor() != 0) {
            final AbstractGameObject below = getObjectStandingOn();
            if (below != null) {
                //
            }
        }
    }

    public void playerLogic() {
        if (getPositionY() > 0) {
            setPositionY(getPositionY() - verticalMotion);
        } else if (getPositionY() < -10) {
            respawnPlayer();
            return;
        }
        if (getPositionY() > Game.GAME_DIMENSION.height + 500) {
            respawnPlayer();
            return;
        }

        setPositionX(getPositionX() + horizontalMotion);

        if (isBlocking() && canBlock()) {
            horizontalMotion /= 2;
        }
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

        handleCollision();


        if (isOnGround()) {
            jumpCounter = 0;
        }

        if (!InputListener.KEY_LIST.contains(keyBindMap.get(Action.ATTACK).getKeyCode())) {
            canAttack = true;
        }

        if (!InputListener.KEY_LIST.contains(keyBindMap.get(Action.CLIP).getKeyCode())) {
            canClip = true;
        }

        if (!InputListener.KEY_LIST.contains(keyBindMap.get(Action.JUMP).getKeyCode())) {
            canJump = true;
        }

        setBlocking(false);

        if (canAttack || attacking && attackTimeHelper.hasPassed(200)) {
            attacking = false;
            attackTimeHelper.reset();
        }

        final List<Integer> keyList = new ArrayList<>(InputListener.KEY_LIST);
        for (Integer integer : keyList) {
            if (keyBindPressed(Action.CLIP, integer)) {
                if (canClip) {
                    final AbstractGameObject gameObject = getObjectStandingOn();
                    if (gameObject != null && gameObject.isPassThrough()) {
                        setPositionY(getPositionY() + 2 + getHeight() / 2 + getObjectStandingOn().getHeight() / 2);
                    }
                    canClip = false;
                }
            } else if (keyBindPressed(Action.ATTACK, integer)) {
                if (canAttack) {
                    attacking = true;
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
                    canAttack = false;
                }
            } else if (keyBindPressed(Action.BLOCK, integer)) {
                setBlocking(true);
                setHorizontalMotion(getHorizontalMotion() / 2);
            } else if (keyBindPressed(Action.FORWARDS, integer)) {
                setAttackFacing(Direction.RIGHT);
                move(Direction.RIGHT);
            } else if (keyBindPressed(Action.BACKWARDS, integer)) {
                setAttackFacing(Direction.LEFT);
                move(Direction.LEFT);
            } else if (keyBindPressed(Action.UP, integer)) {
                setAttackFacing(Direction.UP);
            } else if (keyBindPressed(Action.DOWN, integer)) {
                setAttackFacing(Direction.DOWN);
            } else if (keyBindPressed(Action.JUMP, integer)) {
                if (canJump && ++jumpCounter <= MAX_AIR_JUMPS) {
                    verticalMotion = 30;
                    canJump = false;
                    horizontalMotion += 2 * facing.getHorizontalFactor();
                    Game.instance.onEvent(playerJumpEvent);
                }
            }
        }
        for (Controller controller : InputListener.CONTROLLER_LIST) {
            controller.poll();
            if (controller.getType() == Controller.Type.GAMEPAD) {
                for (Component component : controller.getComponents()) {
                    if (component.getIdentifier().getName().equals("0"))
                        System.out.println(component.getPollData());
                    if (!controllerButtonPressed(Action.ATTACK, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        canAttack = true;
                    }

                    if (!controllerButtonPressed(Action.CLIP, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        canClip = true;
                    }

                    if (!controllerButtonPressed(Action.JUMP, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        canJump = true;
                    }
                    if (controllerButtonPressed(Action.CLIP, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        if (canClip) {
                            final AbstractGameObject gameObject = getObjectStandingOn();
                            if (gameObject != null && gameObject.isPassThrough()) {
                                setPositionY(getPositionY() + 2 + getHeight() / 2 + getObjectStandingOn().getHeight() / 2);
                            }
                            canClip = false;
                        }
                    } else if (controllerButtonPressed(Action.ATTACK, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        if (canAttack) {
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
                            canAttack = false;
                        }
                    } else if (controllerButtonPressed(Action.BLOCK, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        setBlocking(true);
                        setHorizontalMotion(getHorizontalMotion() / 2);
                    } else if (controllerButtonPressed(Action.FORWARDS, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        setAttackFacing(Direction.RIGHT);
                        move(Direction.RIGHT);
                    } else if (controllerButtonPressed(Action.BACKWARDS, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        setAttackFacing(Direction.LEFT);
                        move(Direction.LEFT);
                    } else if (controllerButtonPressed(Action.UP, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        setAttackFacing(Direction.UP);
                    } else if (controllerButtonPressed(Action.DOWN, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        setAttackFacing(Direction.DOWN);
                    } else if (controllerButtonPressed(Action.JUMP, new Tuple<>(component.getIdentifier().getName(), component.getPollData()))) {
                        if (canJump && ++jumpCounter <= MAX_AIR_JUMPS) {
                            verticalMotion = 30;
                            canJump = false;
                            horizontalMotion += 2 * facing.getHorizontalFactor();
                            Game.instance.onEvent(playerJumpEvent);
                        }
                    }
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
            final GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(lastDamageSource == null ? Game.instance.getObjectHandler().getGameObjects().stream().filter(gameObject -> gameObject instanceof AbstractPlayerObject && gameObject != this).findFirst().orElse(null) : lastDamageSource);
            Game.instance.onEvent(gameFinishedEvent);
        }
    }

    private void respawnPlayer() {
        lives--;
        healthPoints = spawnHealth;
        setPositionX(mathHelper.getRandomInt(50, Game.GAME_DIMENSION.width - 50));
        setPositionY(spawnY);
    }

    public AbstractPlayerObject getRayTrace(int distance, Direction direction) {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            if (gameObject instanceof AbstractPlayerObject && gameObject != this) {
                final AxisAligned objectsAxisAligned = new AxisAligned(gameObject.getPositionX(), gameObject.getPositionX() + gameObject.getWidth(), gameObject.getPositionY(), gameObject.getPositionY() + gameObject.getHeight());
                switch (direction) {
                    case DOWN, UP -> {
                        if (playerAxisAligned.getMaxX() >= objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() <= objectsAxisAligned.getMaxX()) {
                            if (mathHelper.getDistanceTo(this, gameObject) <= distance) {
                                return (AbstractPlayerObject) gameObject;
                            }
                        }
                    }
                    case RIGHT -> {
                        if (getEyePosY() >= objectsAxisAligned.getMinY() && getEyePosY() <= objectsAxisAligned.getMaxY()) {
                            if (playerAxisAligned.getMaxX() >= objectsAxisAligned.getMinX() && playerAxisAligned.getMinX() + getWidth() / 2 <= objectsAxisAligned.getMaxX()) {
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

    public void handleCollision() {
        final AxisAligned playerAxisAligned = new AxisAligned(this.getPositionX(), this.getPositionX() + this.getWidth(), this.getPositionY(), this.getPositionY() + this.getHeight());
        onGround = false;
        for (AbstractGameObject gameObject : Game.instance.getObjectHandler().getGameObjects()) {
            if (gameObject.getType() == Type.NON_COLLIDABLE)
                break;

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
     /*   if (direction == Direction.LEFT && facing.getHorizontalFactor() != -1) {
            return;
        }else if(direction == Direction.RIGHT && facing.getHorizontalFactor() != 1) {
            return;
        }*/
        horizontalMotion += 2 * facing.getHorizontalFactor();
        horizontalMotion *= 1.2;
        horizontalMotion = Math.min(5, horizontalMotion);
        horizontalMotion = Math.max(-5, horizontalMotion);
    }

    private void correctFactor() {
        switch (getFacing()) {
            case LEFT -> getFacing().setHorizontalFactor(Direction.LEFT.getHorizontalFactor());
            case RIGHT -> getFacing().setHorizontalFactor(Direction.RIGHT.getHorizontalFactor());
        }
    }

    private void addKeyBinds() {
        switch (this.getId()) {
            case 0 -> {
                keyBindMap.put(Action.FORWARDS, new KeyBind(new Tuple<>("x", 1f), KeyEvent.VK_D));
                keyBindMap.put(Action.BACKWARDS, new KeyBind(new Tuple<>("x", -1f), KeyEvent.VK_A));
                keyBindMap.put(Action.JUMP, new KeyBind(new Tuple<>("0", 1f), KeyEvent.VK_SPACE));
                keyBindMap.put(Action.CLIP, new KeyBind(new Tuple<>("ry", 1f), KeyEvent.VK_Y));
                keyBindMap.put(Action.ATTACK, new KeyBind(new Tuple<>("1", 1f), KeyEvent.VK_X));
                keyBindMap.put(Action.BLOCK, new KeyBind(new Tuple<>("z", 1f), KeyEvent.VK_C));
                keyBindMap.put(Action.DOWN, new KeyBind(new Tuple<>("y", -1f), KeyEvent.VK_S));
                keyBindMap.put(Action.UP, new KeyBind(new Tuple<>("y", 1f), KeyEvent.VK_W));
            }
            case 1 -> {
                keyBindMap.put(Action.FORWARDS, new KeyBind(new Tuple<>("x", 1f), KeyEvent.VK_RIGHT));
                keyBindMap.put(Action.BACKWARDS, new KeyBind(new Tuple<>("x", -1f), KeyEvent.VK_LEFT));
                keyBindMap.put(Action.JUMP, new KeyBind(new Tuple<>("0", 1f), KeyEvent.VK_SHIFT));
                keyBindMap.put(Action.CLIP, new KeyBind(new Tuple<>("ry", 1f), KeyEvent.VK_DOWN));
                keyBindMap.put(Action.ATTACK, new KeyBind(new Tuple<>("1", 1f), KeyEvent.VK_CONTROL));
                keyBindMap.put(Action.BLOCK, new KeyBind(new Tuple<>("z", 1f), KeyEvent.VK_NUMPAD0));
                keyBindMap.put(Action.DOWN, new KeyBind(new Tuple<>("y", -1f), KeyEvent.VK_DOWN));
                keyBindMap.put(Action.UP, new KeyBind(new Tuple<>("y", 1f), KeyEvent.VK_UP));
            }
        }
    }

    private boolean keyBindPressed(Action action, int key) {
        return Objects.equals(key, keyBindMap.get(action).getKeyCode());
    }

    private boolean controllerButtonPressed(Action action, Tuple<String, Float> controller) {
        return (controller.x.equals(keyBindMap.get(action).getControllerTuple().x) && Math.signum(Math.round(controller.y * 100)) == Math.signum(Math.round(keyBindMap.get(action).getControllerTuple().y * 100)) && Math.abs(Math.round(controller.y * 100)) > 20);
    }

    private boolean canExecuteAction(Action action, int keyCode, Tuple<String, Float> controller) {
        return Objects.equals(keyCode, keyBindMap.get(action).getKeyCode()) || (controller.x.equals(keyBindMap.get(action).getControllerTuple().x) && Math.signum(Math.round(controller.y * 100)) == Math.signum(Math.round(keyBindMap.get(action).getControllerTuple().y * 100)) && Math.abs(Math.round(controller.y * 100)) > 20);
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
    public int calculateKnockBack(int percentage, int damage, int weight, int knockBackScaling,
                                  int baseKnockBack,
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

    public Map<Action, KeyBind> getKeyBindMap() {
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

