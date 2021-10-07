package de.itg.wahlkampf;

import de.itg.wahlkampf.object.GameObject;
import de.itg.wahlkampf.object.ObjectHandler;
import de.itg.wahlkampf.object.objects.Player;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas {
    private static final String GAME_TITLE = "Wahlkampf";
    private static final Dimension GAME_DIMENSION = new Dimension(1080,720);

    public ObjectHandler objectHandler;
    private Window window;
    public Player playerObject;

    public Game() {
        window = new Window(GAME_TITLE,GAME_DIMENSION.width, GAME_DIMENSION.height, this);
        onRender();
    }
    public synchronized void start() {

    }
    private void onRender() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics graphics = bufferStrategy.getDrawGraphics();

        Wrapper.WRAPPER_INSTANCE.renderer.textWithShadow(graphics, GAME_TITLE, 1, 10, Color.white);

        playerObject.onRender(graphics);

        for (GameObject gameObject : objectHandler.getGameObjects()) {
            gameObject.onRender(graphics);
        }

        graphics.dispose();

        bufferStrategy.show();
    }
}
