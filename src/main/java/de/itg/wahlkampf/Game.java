package de.itg.wahlkampf;

import de.itg.wahlkampf.object.GameObject;
import de.itg.wahlkampf.object.ObjectHandler;
import de.itg.wahlkampf.object.Type;
import de.itg.wahlkampf.utilities.InputListener;
import de.itg.wahlkampf.utilities.particlesystem.Particle;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {
    private static final String GAME_TITLE = "Wahlkampf";
    private static final Dimension GAME_DIMENSION = new Dimension(1080, 720);
    private static final double UPDATE_CAP = 1.0 / 60.0;
    public static Game instance;

    public ObjectHandler objectHandler;
    private final Window window;
    private boolean running;
    private Thread thread;

    private float framesPerSecond = 60;

    public Game() {
        instance = this;
        this.addKeyListener(new InputListener(this));
        window = new Window(GAME_TITLE, GAME_DIMENSION.width, GAME_DIMENSION.height, this);
        objectHandler = new ObjectHandler();
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
        objectHandler.getGameObjects().forEach(GameObject::onTick);
    }

    private void onRender() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();
        Wrapper.WRAPPER_INSTANCE.renderer.drawFillRectangle(graphics, 0, 0, GAME_DIMENSION.width, GAME_DIMENSION.height, Color.black);
        Wrapper.WRAPPER_INSTANCE.renderer.textWithShadow(graphics, GAME_TITLE, 1, 10, Color.white);
        Wrapper.WRAPPER_INSTANCE.renderer.textWithShadow(graphics, "FPS: " + framesPerSecond, 1, 25, Color.white);
        if (objectHandler == null)
            return;
        for (GameObject gameObject : objectHandler.getGameObjects()) {
            gameObject.onRender(graphics);
            if (gameObject.getType() == Type.PLAYER) {
                Wrapper.WRAPPER_INSTANCE.renderer.textWithShadow(graphics, gameObject.getName(), gameObject.getPositionX(), gameObject.getPositionY(), Color.WHITE);
            }
        }

        for (Particle particle : Wrapper.WRAPPER_INSTANCE.particleHandler.getParticleList()) {
            particle.drawParticle(graphics);
        }

        graphics.dispose();

        bufferStrategy.show();
    }

    public ObjectHandler getObjectHandler() {
        return objectHandler;
    }

    public boolean isFocused() {
        return window.isFocused();
    }
}
