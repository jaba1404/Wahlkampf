package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.effect.AbstractEffect;
import de.itg.wahlkampf.event.impl.GameFinishedEvent;
import de.itg.wahlkampf.event.impl.PlayerAttackEvent;
import de.itg.wahlkampf.event.impl.PlayerBlockEvent;
import de.itg.wahlkampf.event.impl.PlayerJumpEvent;
import de.itg.wahlkampf.object.boundingbox.AxisAligned;
import de.itg.wahlkampf.utilities.*;
import de.itg.wahlkampf.utilities.character.PlayerImage;
import de.itg.wahlkampf.utilities.inputhandling.KeyboardListener;
import net.java.games.input.Component;
import net.java.games.input.Controller;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.*;


public abstract class AbstractPlayerObject extends AbstractGameObject {

    public static final int INITIAL_LIVES = 3;
    private static final int INITIAL_BLOCKS = 3;
    private static final int MAX_AIR_JUMPS = 3;
    private static final long BLOCK_DELAY = 2000;
    private static final int MAX_SPEED = 6;

    private final List<AbstractEffect> effectList;

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
    private final Map<String, PlayerImage> imageMap;
    private int healthPoints;
    private int lives = INITIAL_LIVES;
    private int blockCounter;
    private final int id;
    private final Color color;
    private Direction facing = Direction.RIGHT;
    private Direction attackFacing = Direction.RIGHT;
    private AbstractPlayerObject lastDamageSource;
    private final int spawnHealth, spawnY;
    private int strength;
    private final int baseStrength;
    private final int baseHealthPoints;
    private int verticalMotion;
    private int horizontalMotion;
    private int jumpCounter;

    private final int difference;

    private final Map<Action, KeyBind> keyBindMap;
    private final List<Component> componentList;
    private Controller controller;

    public AbstractPlayerObject(String name, Map<String, PlayerImage> imageMap, int id, int healthPoints, int strength, int weight, int positionX, int positionY, int width, int height) {
        super(name, Type.COLLIDABLE, positionX, positionY, width, height, true);
        this.timeHelper = new TimeHelper();
        this.attackTimeHelper = new TimeHelper();
        this.keyBindMap = new HashMap<>();
        this.imageMap = imageMap;
        this.id = id;
        this.healthPoints = this.baseHealthPoints = healthPoints;
        this.movement = new Movement();
        this.mathHelper = new MathHelper();
        this.spawnHealth = healthPoints;
        this.spawnY = positionY;
        this.strength = this.baseStrength = strength;

        effectList = new ArrayList<>();
        componentList = new ArrayList<>();

        playerAttackEvent = new PlayerAttackEvent(this);
        playerJumpEvent = new PlayerJumpEvent(this);

        addKeyBinds();

        switch (id) {
            case 0 -> color = Color.BLUE;
            case 1 -> color = Color.RED;
            case 2 -> color = Color.GREEN;
            case 3 -> color = Color.YELLOW;
            case 4 -> color = Color.MAGENTA;
            default -> color = Color.WHITE;
        }

        difference = Math.abs(imageMap.get("hit").getBufferedImage().getWidth() - imageMap.get("stand").getBufferedImage().getWidth());
    }

    /* onAttack Methode Abstract, damit jeder Charakter später eventuelle individuelle Special-Attacks bekommen kann  */
    public abstract void onAttack(AbstractPlayerObject enemy);

    @Override
    public void onRender(Graphics graphics) {
        final PlayerImage playerImage = attacking ? imageMap.get("hit") : imageMap.get("stand");
        final BufferedImage playerImageAdjusted = facing == Direction.RIGHT ? playerImage.getBufferedImage() : playerImage.getBufferedImageFlippedHorizontal();
        getRenderer().img(graphics, playerImageAdjusted, attacking ? getFacing() == Direction.LEFT ? (getPositionX() - difference / 4) - 5 : getPositionX() + 5 : getPositionX(), getPositionY(), attacking ? getWidth() + difference / 4 : getWidth(), getHeight());
        if (isBlocking() && canBlock()) {
            getRenderer().drawFillCircle(graphics, getPositionX() - 5, getPositionY() - 5, getWidth() + 10, getHeight() + 10, new Color(118, 231, 118, 150));
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
        if (horizontalMotion > 0) {
            setFacing(Direction.RIGHT);
        } else if (horizontalMotion < 0) {
            setFacing(Direction.LEFT);
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

        setBlocking(false);

        if (canAttack || attacking && attackTimeHelper.hasPassed(200)) {
            attacking = false;
            attackTimeHelper.reset();
        }

        if (controller != null) {
            Game.instance.getControllerListener().handleController(controller, componentList);
        }

        handleInputs();

        if (!canBlock() || !isBlocking()) {
            if (timeHelper.hasPassed(BLOCK_DELAY)) {
                blockCounter = 0;
                timeHelper.reset();
            }
        } else {
            timeHelper.reset();
        }

        if (!effectList.isEmpty()) {
            effectList.removeIf(AbstractEffect::isToBeRemoved);
            effectList.stream().filter(abstractEffect -> !abstractEffect.isToBeRemoved()).forEach(abstractEffect -> {
                if (abstractEffect.getTimeHelper().hasPassed(abstractEffect.getDuration())) {
                    System.out.println(abstractEffect.getTimeHelper().getDifference());
                    abstractEffect.removeEffect();
                } else {
                    abstractEffect.onEffect();
                }
            });
        }

        if (healthPoints <= 0 && lives > 0) {
            respawnPlayer();
        }
        if (lives <= 0) {
            deleteObject();
            if (Game.instance.getObjectHandler().getGameObjects().stream().filter(abstractGameObject -> abstractGameObject instanceof AbstractPlayerObject && ((AbstractPlayerObject) abstractGameObject).getLives() > 0).toList().size() -1 < 1) {
                final GameFinishedEvent gameFinishedEvent = new GameFinishedEvent(lastDamageSource == null ? Game.instance.getObjectHandler().getGameObjects().stream().filter(gameObject -> gameObject instanceof AbstractPlayerObject && gameObject != this).findFirst().orElse(null) : lastDamageSource);
                Game.instance.onEvent(gameFinishedEvent);
            }
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
        if (direction.getHorizontalFactor() != 0) {
            this.facing = direction;
        }
        if (Math.abs(horizontalMotion) < MAX_SPEED) {
            horizontalMotion += 2 * facing.getHorizontalFactor();
            horizontalMotion *= 1.2;
            //falls horizontalMotion den max. Movement-Speed überschreiten sollte wird hier die Motion nochmal auf die maximale Geschwindigkeit angepasst
            if (Math.abs(horizontalMotion) >= MAX_SPEED) {
                horizontalMotion = Math.min(MAX_SPEED, horizontalMotion);
                horizontalMotion = Math.max(-MAX_SPEED, horizontalMotion);
            }
        }
    }

    private void addKeyBinds() {
        //add Controller binds
        keyBindMap.put(Action.FORWARDS, new KeyBind(new Tuple<>("x", "+")));
        keyBindMap.put(Action.BACKWARDS, new KeyBind(new Tuple<>("x", "-")));
        keyBindMap.put(Action.JUMP, new KeyBind(new Tuple<>("0", "+")));
        keyBindMap.put(Action.CLIP, new KeyBind(new Tuple<>("ry", "+")));
        keyBindMap.put(Action.ATTACK, new KeyBind(new Tuple<>("1", "+")));
        keyBindMap.put(Action.BLOCK, new KeyBind(new Tuple<>("z", "+-")));
        keyBindMap.put(Action.DOWN, new KeyBind(new Tuple<>("y", "-")));
        keyBindMap.put(Action.UP, new KeyBind(new Tuple<>("y", "+")));

        //add Keyboard binds to existing Map Entry
        switch (this.getId()) {
            case 0 -> {
                keyBindMap.get(Action.FORWARDS).setKeyCode(KeyEvent.VK_D);
                keyBindMap.get(Action.BACKWARDS).setKeyCode(KeyEvent.VK_A);
                keyBindMap.get(Action.JUMP).setKeyCode(KeyEvent.VK_SPACE);
                keyBindMap.get(Action.CLIP).setKeyCode(KeyEvent.VK_Y);
                keyBindMap.get(Action.ATTACK).setKeyCode(KeyEvent.VK_X);
                keyBindMap.get(Action.BLOCK).setKeyCode(KeyEvent.VK_C);
                keyBindMap.get(Action.DOWN).setKeyCode(KeyEvent.VK_S);
                keyBindMap.get(Action.UP).setKeyCode(KeyEvent.VK_W);
            }
            case 1 -> {
                keyBindMap.get(Action.FORWARDS).setKeyCode(KeyEvent.VK_RIGHT);
                keyBindMap.get(Action.BACKWARDS).setKeyCode(KeyEvent.VK_LEFT);
                keyBindMap.get(Action.JUMP).setKeyCode(KeyEvent.VK_SHIFT);
                keyBindMap.get(Action.CLIP).setKeyCode(KeyEvent.VK_DOWN);
                keyBindMap.get(Action.ATTACK).setKeyCode(KeyEvent.VK_CONTROL);
                keyBindMap.get(Action.BLOCK).setKeyCode(KeyEvent.VK_NUMPAD0);
                keyBindMap.get(Action.DOWN).setKeyCode(KeyEvent.VK_DOWN);
                keyBindMap.get(Action.UP).setKeyCode(KeyEvent.VK_UP);
            }
        }
    }

    private boolean keyBindPressed(Action action, int key) {
        return Objects.equals(key, keyBindMap.get(action).getKeyCode());
    }

    private boolean checkControllerInputValue(String input, float value) {
        final float valueToCheck = Math.signum(Math.round(value * 100));
        switch (input) {
            case "+" -> {
                if (valueToCheck == 1) {
                    return true;
                }
            }
            case "-" -> {
                if (valueToCheck == -1) {
                    return true;
                }
            }
            case "+-", "-+" -> {
                if (value != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean controllerButtonPressed(Action action, Component component) {
        return (component.getIdentifier().getName().equals(keyBindMap.get(action).getControllerTuple().x) && checkControllerInputValue(keyBindMap.get(action).getControllerTuple().y, component.getPollData()));
    }

    private void executeAction(Action action) {
        switch (action) {
            case CLIP -> {
                if (canClip) {
                    final AbstractGameObject gameObject = getObjectStandingOn();
                    if (gameObject != null && gameObject.isPassThrough()) {
                        setPositionY(getPositionY() + 2 + getHeight() / 2 + getObjectStandingOn().getHeight() / 2);
                    }
                    canClip = false;
                }
            }
            case ATTACK -> {
                if (canAttack) {
                    attacking = true;
                    final AbstractPlayerObject rayTraced = getRayTrace(attackFacing == Direction.DOWN || attackFacing == Direction.UP ? 60 : 40, attackFacing);
                    if (rayTraced != null) {
                        onAttack(rayTraced);
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
            }
            case BLOCK -> {
                setBlocking(true);
                setHorizontalMotion(getHorizontalMotion() / 2);
            }
            case FORWARDS -> {
                setAttackFacing(Direction.RIGHT);
                move(Direction.RIGHT);
            }
            case BACKWARDS -> {
                setAttackFacing(Direction.LEFT);
                move(Direction.LEFT);
            }
            case UP -> {
                setAttackFacing(Direction.UP);
            }
            case DOWN -> {
                setAttackFacing(Direction.DOWN);
            }
            case JUMP -> {
                if (canJump && ++jumpCounter <= MAX_AIR_JUMPS) {
                    verticalMotion = 30;
                    canJump = false;
                    horizontalMotion += 2 * facing.getHorizontalFactor();
                    Game.instance.onEvent(playerJumpEvent);
                }
            }
        }
    }

    private void handleInputs() {
        if (keyBindMap.isEmpty()) {
            return;
        }
        if ((!KeyboardListener.KEY_LIST.contains(keyBindMap.get(Action.ATTACK).getKeyCode()) && componentList.stream().filter(component -> component.getIdentifier().getName().equals(keyBindMap.get(Action.ATTACK).getControllerTuple().x)).toList().isEmpty())) {
            canAttack = true;
        }

        if ((!KeyboardListener.KEY_LIST.contains(keyBindMap.get(Action.CLIP).getKeyCode()) && componentList.stream().filter(component -> component.getIdentifier().getName().equals(keyBindMap.get(Action.CLIP).getControllerTuple().x)).toList().isEmpty())) {
            canClip = true;
        }

        if ((!KeyboardListener.KEY_LIST.contains(keyBindMap.get(Action.JUMP).getKeyCode()) && componentList.stream().filter(component -> component.getIdentifier().getName().equals(keyBindMap.get(Action.JUMP).getControllerTuple().x)).toList().isEmpty())) {
            canJump = true;
        }

        final List<Integer> keyList = new ArrayList<>(KeyboardListener.KEY_LIST);
        if (!keyList.isEmpty() || !componentList.isEmpty()) {
            for (Action action : Action.values()) {
                for (Integer keyCode : keyList) {
                    if (keyBindPressed(action, keyCode)) {
                        executeAction(action);
                    }
                }
                for (Component component : componentList) {
                    if (controllerButtonPressed(action, component)) {
                        executeAction(action);
                    }
                }
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

    public boolean canBlock() {
        return this.getBlockCounter() < INITIAL_BLOCKS;
    }

    public int getLives() {
        return lives;
    }

    public void setLives(int lives) {
        this.lives = lives;
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

    public Color getColor() {
        return color;
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

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getBaseStrength() {
        return baseStrength;
    }

    public int getBaseHealthPoints() {
        return baseHealthPoints;
    }

    public List<AbstractEffect> getEffectList() {
        return effectList;
    }

    public void addEffects(AbstractEffect effect) {
        effectList.removeIf(effect1 -> effect.getName().equals(effect1.getName()));
        effectList.add(effect);
    }

    public Controller getController() {
        return controller;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }
}

