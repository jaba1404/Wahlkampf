package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.AddPlayerObjectsEvent;
import de.itg.wahlkampf.object.objects.StageBlock;
import de.itg.wahlkampf.object.players.MerkelPlayer;
import de.itg.wahlkampf.object.players.TrumpPlayer;
import de.itg.wahlkampf.utilities.MathHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ObjectHandler {

    private final ArrayList<AbstractGameObject> gameObjects = new ArrayList<>();
    private final MathHelper mathHelper;
    private int x = 200;
    private int y = 500;
    private BufferedImage[] dirtBlocks;

    public ObjectHandler() {
        try {
            dirtBlocks = new BufferedImage[]{ImageIO.read(new File("resources\\grass_block_left.png")), ImageIO.read(new File("resources\\grass_block.png")), ImageIO.read(new File("resources\\grass_block_right.png"))};
        } catch (IOException e) {
            e.printStackTrace();
        }
        mathHelper = new MathHelper();
        for (int i = 0; i < 2; i++) {
            int width = mathHelper.getRandomInt(100, 600);
            int width2 = mathHelper.getRandomInt(100, 600);
            addObject(new StageBlock(x, y, width, 30, dirtBlocks, true));
            addObject(new StageBlock(x, y - 150, width2, 30, dirtBlocks, true));
            x += mathHelper.getRandomInt(width, width + 200);
        }
        addObject(new StageBlock(0, 660, Game.instance.getWidth(), 30, new Color(0, 0, 0, 0), false));
    }

    public void addPlayerObjects(String[] players) {
        for (int i = 0; i < players.length; i++) {
            final String s = players[i];
            switch (s) {
                case "Merkel" -> addObject(new MerkelPlayer(i));
                case "Trump" -> addObject(new TrumpPlayer(i));
            }
        }
        final AddPlayerObjectsEvent addPlayerObjectsEvent = new AddPlayerObjectsEvent(gameObjects.stream().filter(abstractGameObject -> abstractGameObject.getType() == Type.PLAYER).collect(Collectors.toList()));
        Game.instance.onEvent(addPlayerObjectsEvent);
    }

    public ArrayList<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(AbstractGameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }
}
