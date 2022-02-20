package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.AddPlayerObjectsEvent;
import de.itg.wahlkampf.object.objects.StageBlock;
import de.itg.wahlkampf.object.players.MerkelPlayer;
import de.itg.wahlkampf.object.players.TrumpPlayer;
import de.itg.wahlkampf.utilities.MathHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ObjectHandler {

    private final ArrayList<AbstractGameObject> gameObjects = new ArrayList<>();
    private final MathHelper mathHelper;
    private int x = 200, x2 = x;
    private int y = 500;
    private BufferedImage[] dirtBlocks;

    public ObjectHandler() {
        try {
            dirtBlocks = new BufferedImage[]{
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/grass_block_left.png")),
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/grass_block.png")),
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/grass_block_right.png"))
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        mathHelper = new MathHelper();
        for (int i = 0; i < 2; i++) {
            int width = Math.round(mathHelper.getRandomInt(100, 600) / 30f) * 30;
            int width2 = Math.round(mathHelper.getRandomInt(100, 600) / 30f) * 30;
            addObject(new StageBlock(x, y, width, 30, dirtBlocks, true));
            addObject(new StageBlock(x2, y - 150, width2, 30, dirtBlocks, true));
            x += mathHelper.getRandomInt(width + 100, width + 200);
            x2 += mathHelper.getRandomInt(width2 + 100, width2 + 200);
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
