package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.objects.StageBlock;
import de.itg.wahlkampf.object.players.Player;
import de.itg.wahlkampf.object.players.TestPlayer;
import de.itg.wahlkampf.utilities.MathHelper;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ObjectHandler {

    private final ArrayList<AbstractGameObject> gameObjects = new ArrayList<>();
    private int x = 0;
    private int y = 500;

    public ObjectHandler() {
        for (int i = 0; i < 2; i++) {
            int width = MathHelper.getRandomInt(100, 600);
            int width2 = MathHelper.getRandomInt(100, 600);
            try {
                addObject(new StageBlock(x, y, width, 30, new BufferedImage[] {ImageIO.read(new File("resources\\grass_block_left.png")),ImageIO.read(new File("resources\\grass_block.png")),ImageIO.read(new File("resources\\grass_block_right.png"))}));
                addObject(new StageBlock(x, y - 150, width2, 30, new BufferedImage[] {ImageIO.read(new File("resources\\grass_block_left.png")),ImageIO.read(new File("resources\\grass_block.png")),ImageIO.read(new File("resources\\grass_block_right.png"))}));
            } catch (IOException e) {
                e.printStackTrace();
            }
            x += MathHelper.getRandomInt(width, width + 200);
        }
        addObject(new StageBlock(0, 660, 1080, 30,new Color(0,0,0,0)));
        addObject(new Player(0));
        addObject(new TestPlayer(1));

    }

    public ArrayList<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(AbstractGameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }
}
