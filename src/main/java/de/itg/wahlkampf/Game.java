package de.itg.wahlkampf;

import de.itg.wahlkampf.event.Event;
import de.itg.wahlkampf.event.impl.SettingChangeEvent;
import de.itg.wahlkampf.menu.Menu;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.ObjectHandler;
import de.itg.wahlkampf.object.Type;
import de.itg.wahlkampf.setting.SettingManager;
import de.itg.wahlkampf.setting.settings.SettingCheckBox;
import de.itg.wahlkampf.setting.settings.SettingComboBox;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.ImageHelper;
import de.itg.wahlkampf.utilities.InputListener;
import de.itg.wahlkampf.utilities.Renderer;
import de.itg.wahlkampf.utilities.particlesystem.AbstractParticle;
import de.itg.wahlkampf.utilities.particlesystem.ParticleHandler;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Game extends Canvas implements Runnable {
    public static final String GAME_TITLE = "Wahlkampf";
    public static final String GAME_VERSION = "Alpha 1.0.0";
    private static final Dimension GAME_DIMENSION = new Dimension(1280, 720);
    private static final double UPDATE_CAP = 1.0 / 60.0;
    public static Game instance;

    private final ObjectHandler objectHandler;
    private final ImageHelper imageHelper;
    private final Window window;
    private final Renderer renderer;
    private final ParticleHandler particleHandler;
    private final SettingManager settingManager;
    private final Menu menu;
    private boolean running;
    private Thread thread;
    private final SettingCheckBox startGame;
    private final SettingComboBox stageSetting;
    private final List<BufferedImage> bufferedImages;
    private final Map<String, File> backgroundMap;
    private File backgroundFile;

    private float framesPerSecond = 60;
    private final Font textFont = new Font("Roboto", Font.PLAIN, 12);

    public Game() {
        instance = this;
        imageHelper = new ImageHelper();
        bufferedImages = imageHelper.getFrames(new File("resources\\9BC613A2-35B0-488D-B6AC-E217003CA6C8.gif"));
        backgroundMap = Stream.of(new Object[][]{
                {"White House", new File("resources\\hintergrund 1.gif")},
                {"Red Arena", new File("resources\\9BC613A2-35B0-488D-B6AC-E217003CA6C8.gif")},
        }).collect(Collectors.toMap(data -> (String) data[0], data -> (File) data[1]));
        settingManager = new SettingManager();
        startGame = (SettingCheckBox) settingManager.getSettingByName("Start Game");
        stageSetting = (SettingComboBox) settingManager.getSettingByName("Stage");
        backgroundFile = backgroundMap.get(stageSetting.getCurrentOption());

        renderer = new Renderer();
        this.addKeyListener(new InputListener(this));
        window = new Window(GAME_TITLE, GAME_DIMENSION.width, GAME_DIMENSION.height, this);
        menu = new Menu();
        this.addMouseListener(menu);
        this.addMouseMotionListener(menu);
        objectHandler = new ObjectHandler();
        particleHandler = new ParticleHandler();
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

        boolean render;
        double firstTime;
        double lastTime = System.nanoTime() / 1000000000.0;
        double passedTime;
        double unprocessedTime = 0;

        double frameTime = 0;
        int frames = 0;
        int fps;

        while (running) {
            render = false;
            firstTime = System.nanoTime() / 1000000000.0;
            passedTime = firstTime - lastTime;
            lastTime = firstTime;

            unprocessedTime += passedTime;
            frameTime += passedTime;

            while (unprocessedTime >= UPDATE_CAP) {

                unprocessedTime -= UPDATE_CAP;
                render = true;

                if (frameTime >= 1.0) {
                    frameTime = 0;
                    fps = frames;
                    framesPerSecond = fps;
                    frames = 0;
                }
                if (!isFocused()) {
                    if (!InputListener.KEY_LIST.isEmpty()) {
                        InputListener.KEY_LIST.clear();
                        System.out.println("Cleared!");
                    }
                }
                if (startGame.isActive())
                    onTick();
            }

            if (render) {
                //Render game
                onRender();
                frames++;
            } else {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        stop();
    }

    private void onTick() {
        if (objectHandler == null)
            return;
        objectHandler.getGameObjects().forEach(AbstractGameObject::onTick);
    }

    public void onEvent(Event event) {
        if (event instanceof SettingChangeEvent) {
            if (((SettingChangeEvent) event).getTarget() == stageSetting) {
                backgroundFile = backgroundMap.get(((SettingChangeEvent) event).getDstString());
            }
        }
    }

    private void onRender() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        try {
            renderer.img(graphics, ImageIO.read(backgroundFile), 0, 0, GAME_DIMENSION.width, GAME_DIMENSION.height);
        } catch (IOException e) {
            e.printStackTrace();
        }

        renderer.textWithShadow(graphics, GAME_TITLE, 1, 10, Color.white, textFont);
        renderer.textWithShadow(graphics, "FPS: " + framesPerSecond, 1, 25, Color.white, textFont);
        menu.drawScreen(graphics);


        if (objectHandler == null)
            return;
        if (startGame.isActive()) {
            for (AbstractGameObject gameObject : objectHandler.getGameObjects()) {
                gameObject.onRender(graphics);
                if (gameObject.getType() == Type.PLAYER) {
                    renderer.textWithShadow(graphics, gameObject.getName(), gameObject.getPositionX(), gameObject.getPositionY(), Color.WHITE, textFont);
                }
            }
        }
        for (AbstractParticle particle : particleHandler.getParticleList()) {
            particle.drawParticle(graphics);
        }

        graphics.dispose();

        bufferStrategy.show();
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

    public boolean isFocused() {
        return window.isFocused();
    }

    public Map<String, File> getBackgroundMap() {
        return backgroundMap;
    }
}
