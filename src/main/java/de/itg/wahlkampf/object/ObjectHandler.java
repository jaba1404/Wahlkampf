package de.itg.wahlkampf.object;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.AddGameObjectsEvent;
import de.itg.wahlkampf.object.objects.blocks.StageBlock;
import de.itg.wahlkampf.object.objects.players.MerkelPlayer;
import de.itg.wahlkampf.object.objects.players.TrumpPlayer;
import de.itg.wahlkampf.setting.settings.SettingComboBox;
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
    private int x, x2;
    private final int y = 500;
    private BufferedImage[] dirtBlocks;
    private BufferedImage[] metalBlocks;
    private final SettingComboBox stageSetting = (SettingComboBox) Game.instance.getSettingManager().getSettingByName("Stage");

    public ObjectHandler() {
        try {
            dirtBlocks = new BufferedImage[]{
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/blocks/grass/grass_block_left.png")),
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/blocks/grass/grass_block.png")),
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/blocks/grass/grass_block_right.png"))
            };
            metalBlocks = new BufferedImage[]{
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/blocks/metal/metal_left_end.png")),
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/blocks/metal/metal_main.png")),
                    ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/blocks/metal/metal_right_end.png"))
            };
        } catch (IOException e) {
            e.printStackTrace();
        }
        mathHelper = Game.instance.getMathHelper();
        addObject(new StageBlock(0, 660, Game.instance.getWidth(), 30, new Color(0, 0, 0, 0), false));
    }

    public void addGameObjects(String[] players) {
        final String option = stageSetting.getCurrentOption();
        x = mathHelper.getRandomInt(100, 300);
        x2 = mathHelper.getRandomInt(100, 300);
        for (int i = 0; i < 2; i++) {
            final int width = Math.round(mathHelper.getRandomInt(100, 600) / 30f) * 30;
            final int width2 = Math.round(mathHelper.getRandomInt(100, 600) / 30f) * 30;
            final BufferedImage[] blocks = option.equals("White House") ? dirtBlocks : metalBlocks;
            addObject(new StageBlock(x, y, x + width > Game.GAME_DIMENSION.width ? Game.GAME_DIMENSION.width - x - 50 : width, 30, blocks, true));
            addObject(new StageBlock(x2, y - 150, x2 + width2 > Game.GAME_DIMENSION.width ? Game.GAME_DIMENSION.width - x2 - 50 : width2, 30, blocks, true));
            x += mathHelper.getRandomInt(width + 100, width + 200);
            x2 += mathHelper.getRandomInt(width2 + 100, width2 + 200);
        }

        for (int i = 0; i < players.length; i++) {
            final String s = players[i];
            final int posX = mathHelper.getRandomInt(50, Game.GAME_DIMENSION.width - 50);
            final int posY = 50;
            switch (s) {
                case "Merkel" -> addObject(new MerkelPlayer(i, posX, posY));
                case "Trump" -> addObject(new TrumpPlayer(i, posX, posY));
            }
        }

        final AddGameObjectsEvent addGameObjectsEvent = new AddGameObjectsEvent(gameObjects.stream().filter(abstractGameObject -> abstractGameObject instanceof AbstractPlayerObject).collect(Collectors.toList()));
        Game.instance.onEvent(addGameObjectsEvent);

    }

    public ArrayList<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }

    public void addObject(AbstractGameObject gameObject) {
        this.getGameObjects().add(gameObject);
    }
}
