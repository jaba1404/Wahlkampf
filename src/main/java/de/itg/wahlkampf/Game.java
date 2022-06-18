package de.itg.wahlkampf;

import de.itg.wahlkampf.event.AbstractEvent;
import de.itg.wahlkampf.event.impl.*;
import de.itg.wahlkampf.menu.menus.FinishedMenu;
import de.itg.wahlkampf.menu.menus.InGameMenu;
import de.itg.wahlkampf.menu.menus.MainMenu;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.AbstractItemObject;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.object.ObjectHandler;
import de.itg.wahlkampf.object.objects.items.RegenerationItem;
import de.itg.wahlkampf.object.objects.items.StrengthItem;
import de.itg.wahlkampf.setting.SettingManager;
import de.itg.wahlkampf.setting.settings.SettingCheckBox;
import de.itg.wahlkampf.setting.settings.SettingComboBox;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.MathHelper;
import de.itg.wahlkampf.utilities.Renderer;
import de.itg.wahlkampf.utilities.TimeHelper;
import de.itg.wahlkampf.utilities.inputhandling.ControllerListener;
import de.itg.wahlkampf.utilities.inputhandling.KeyboardListener;
import de.itg.wahlkampf.utilities.sound.Sound;
import de.itg.wahlkampf.utilities.sound.SoundHelper;
import net.java.games.input.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game extends Canvas implements Runnable {
    public static final String GAME_TITLE = "Wahlkampf";
    public static final String GAME_VERSION = "Alpha 1.0.0";
    public static final Dimension GAME_DIMENSION = new Dimension(1280, 720);

    public static final int MAX_PLAYER_AMOUNT = 6;

    private static final double UPDATE_CAP = 1.0 / 60.0;
    public static Game instance;

    private final List<String> playerNames = new ArrayList<>();
    private final ObjectHandler objectHandler;
    private final Window window;
    private final Renderer renderer;
    private final KeyboardListener keyboardListener;
    private final ControllerListener controllerListener;
    private final SettingManager settingManager;
    private final SoundHelper soundHelper;
    private final MathHelper mathHelper;
    private MainMenu menu;
    private InGameMenu ingameMenu;
    private final FinishedMenu finishedMenu;
    private boolean running;
    private Thread thread;
    private final SettingCheckBox showFps;
    private final SettingCheckBox startGame;
    private final SettingComboBox stageSetting;
    private final Map<String, URL> backgroundMap;
    private final List<Controller> controllerList;
    private URL backgroundFile;
    private final TimeHelper spawnTimeHelper;
    private long spawnDelay;

    private float framesPerSecond = 60;
    private final Font textFont = new Font("Roboto", Font.PLAIN, 12);

    private Image background;

    public Game() {
        instance = this;
        keyboardListener = new KeyboardListener();
        controllerListener = new ControllerListener();
        soundHelper = new SoundHelper();
        spawnTimeHelper = new TimeHelper();
        spawnTimeHelper.reset();
        mathHelper = new MathHelper();
        spawnDelay = mathHelper.getRandomInt(30000, 45000);
        backgroundMap = Stream.of(new Object[][]{
                {"White House", getClass().getResource("assets/background/white_house.gif")},
                {"Red Arena", getClass().getResource("assets/background/red_arena.gif")},
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (URL) data[1]));
        playerNames.addAll(Arrays.asList("Trump", "Merkel"));
        playerNames.add("None");
        settingManager = new SettingManager();
        showFps = (SettingCheckBox) settingManager.getSettingByName("Show FPS");
        startGame = (SettingCheckBox) settingManager.getSettingByName("Start Game");
        stageSetting = (SettingComboBox) settingManager.getSettingByName("Stage");
        backgroundFile = backgroundMap.get(stageSetting.getCurrentOption());

        renderer = new Renderer();
        this.addKeyListener(keyboardListener);
        window = new Window(GAME_TITLE, GAME_DIMENSION.width, GAME_DIMENSION.height, this);
        objectHandler = new ObjectHandler();
        menu = new MainMenu();
        finishedMenu = new FinishedMenu();
        this.addMouseListener(menu);
        this.addMouseMotionListener(menu);
        this.addMouseListener(finishedMenu);
        this.addMouseMotionListener(finishedMenu);
        controllerList = new ArrayList<>();
        background = new ImageIcon(backgroundFile).getImage();

        for (Controller controller : ControllerListener.CONTROLLER_LIST) {
            if (controller.getType().equals(Controller.Type.GAMEPAD)) {
                controllerList.add(controller);
            }
        }
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        running = true;

        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps;

        while (running) {
            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= UPDATE_CAP) {

                unprocessedTime -= UPDATE_CAP;

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    framesPerSecond = fps;
                    frames = 0;
                }

                if (!isFocused()) {
                    if (!KeyboardListener.KEY_LIST.isEmpty()) {
                        KeyboardListener.KEY_LIST.clear();
                        System.out.println("Cleared!");

                    }
                }
                if (startGame.isActive()) {
                    onTick();
                    if (spawnTimeHelper.hasPassed(spawnDelay)) {
                        if (objectHandler.getGameObjects().stream().filter(abstractGameObject -> abstractGameObject instanceof AbstractItemObject).toArray().length < 4) {
                            switch (mathHelper.getRandomInt(0, 1)) {
                                case 0 -> objectHandler.addObject(new RegenerationItem(mathHelper.getRandomInt(20, GAME_DIMENSION.width - 20), 0));
                                case 1 -> objectHandler.addObject(new StrengthItem(mathHelper.getRandomInt(20, GAME_DIMENSION.width - 20), 0));
                            }
                        }
                        spawnDelay = mathHelper.getRandomInt(25000, 45000);
                        spawnTimeHelper.reset();
                    }

                    objectHandler.getGameObjects().removeIf(AbstractGameObject::isDeleted);
                }
            }
            //Render game
            onRender();
            frames++;
        }

        stop();

    }

    private void onTick() {
        if (objectHandler == null)
            return;
        objectHandler.getGameObjects().stream().filter(gameObject -> !gameObject.isDeleted()).forEach(AbstractGameObject::onTick);
    }

    public void onEvent(AbstractEvent abstractEvent) {
        if (abstractEvent instanceof SettingChangeEvent) {
            if (((SettingChangeEvent) abstractEvent).getTarget() == stageSetting) {
                backgroundFile = backgroundMap.get(((SettingChangeEvent) abstractEvent).getDstString());
                background = new ImageIcon(backgroundFile).getImage();
            }
            soundHelper.playMusic(Sound.SETTINGS_CHANGE.getLocation());
        }
        if (abstractEvent instanceof GameFinishedEvent) {
            if (finishedMenu.getAbstractPlayerObject() == null) {
                finishedMenu.setAbstractPlayerObject((AbstractPlayerObject) ((GameFinishedEvent) abstractEvent).getWinner());
                soundHelper.playMusic(Sound.FINISHED.getLocation());
                finishedMenu.setVisible(true);
                getObjectHandler().getGameObjects().stream().filter(gameObject -> gameObject instanceof AbstractPlayerObject).forEach(AbstractGameObject::deleteObject);
            }
        }
        if (abstractEvent instanceof AddGameObjectsEvent) {
            ingameMenu = new InGameMenu(((AddGameObjectsEvent) abstractEvent).getPlayerObjects());
            if (!controllerList.isEmpty()) {
                for (int i = 0; i < ((AddGameObjectsEvent) abstractEvent).getPlayerObjects().size(); i++) {
                    final AbstractPlayerObject playerObject = (AbstractPlayerObject) ((AddGameObjectsEvent) abstractEvent).getPlayerObjects().get(i);
                    if (playerObject != null) {
                        if (controllerList.size() > i) {
                            playerObject.setController(controllerList.get(i));
                        } else {
                            System.out.println("not enough controller");
                        }
                    }
                }
            }
        }
        if (abstractEvent instanceof PlayerAttackEvent) {
            soundHelper.playMusic(Sound.HIT.getLocation());
        }
        if (abstractEvent instanceof PlayerJumpEvent) {
            soundHelper.playMusic(Sound.JUMP.getLocation());
        }
        if (abstractEvent instanceof PlayerBlockEvent) {
            soundHelper.playMusic(Sound.BLOCK.getLocation());
        }
    }

    private void onRender() {
        final BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        final Graphics graphics = bufferStrategy.getDrawGraphics();
        renderer.img(graphics, background, 0, 0, GAME_DIMENSION.width, GAME_DIMENSION.height);
        if (showFps.isActive()) {
            renderer.textWithShadow(graphics, "FPS: " + framesPerSecond, 1, GAME_DIMENSION.height - 40, Color.white, textFont);
        }
        if (menu != null) {
            menu.drawScreen(graphics);
        }

        if (objectHandler == null)
            return;
        if (startGame.isActive()) {
            objectHandler.getGameObjects().stream().filter(gameObject -> !gameObject.isDeleted()).forEach(gameObject -> {
                gameObject.onRender(graphics);
                if (gameObject instanceof AbstractPlayerObject) {
                    ingameMenu.drawScreen(graphics);
                    renderer.drawFillRectangle(graphics, (int) (gameObject.getPositionX() - gameObject.getWidth() + textFont.getStringSize(gameObject.getName()).getWidth() / 2) - 4, (int) (gameObject.getPositionY() - 5 - (textFont.getStringSize(gameObject.getName()).getHeight() / 2)) - 4, (int) textFont.getStringSize(gameObject.getName()).getWidth() + 8, (int) (textFont.getStringSize(gameObject.getName()).getHeight() / 2) +8, new Color(0,0,0,75));
                    renderer.textWithShadow(graphics, gameObject.getName(), (int) (gameObject.getPositionX() - gameObject.getWidth() + textFont.getStringSize(gameObject.getName()).getWidth() / 2), gameObject.getPositionY() - 5, ((AbstractPlayerObject) gameObject).getColor(), textFont);
                }
            });
            menu = null;
        }

        if (finishedMenu != null && finishedMenu.isVisible()) {
            finishedMenu.drawScreen(graphics);
        }
        graphics.dispose();

        bufferStrategy.show();
    }

    public MathHelper getMathHelper() {
        return mathHelper;
    }

    public ObjectHandler getObjectHandler() {
        return objectHandler;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public SettingManager getSettingManager() {
        return settingManager;
    }

    public ControllerListener getControllerListener() {
        return controllerListener;
    }

    public boolean isFocused() {
        return window.isFocused();
    }

    public List<String> getPlayerNames() {
        return playerNames;
    }

    public Map<String, URL> getBackgroundMap() {
        return backgroundMap;
    }
}
