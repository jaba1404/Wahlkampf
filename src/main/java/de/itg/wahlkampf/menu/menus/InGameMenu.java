package de.itg.wahlkampf.menu.menus;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.IOException;
import java.util.List;

public class InGameMenu implements IMenu {
    private final List<AbstractGameObject> gameObjects;
    private final Renderer renderer;
    private final Font characterName;
    private final BufferedImage itemImageGray;
    private BufferedImage itemImage;

    public InGameMenu(List<AbstractGameObject> gameObjects) {
        this.gameObjects = gameObjects;
        renderer = Game.instance.getRenderer();
        characterName = new Font("Roboto", Font.BOLD, 25);
        try {
            this.itemImage = ImageIO.read(Game.class.getResource("/de/itg/wahlkampf/assets/items/heart_item.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int width = itemImage.getWidth();
        int height = itemImage.getHeight();
        itemImageGray = new BufferedImage(width, height, BufferedImage.TRANSLUCENT);
        final ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
        op.filter(itemImage, itemImageGray);
    }

    @Override
    public void drawScreen(Graphics graphics) {
        int x = 50;
        int healthBarWidth = 120;
        int healthBarHeight = 20;
        for (AbstractGameObject gameObject : gameObjects) {
            final AbstractPlayerObject abstractPlayerObject = (AbstractPlayerObject) gameObject;
            int y = 25;
            renderer.drawFillRectangle(graphics, (int) ((x + healthBarWidth / 2) - characterName.getStringSize(abstractPlayerObject.getName()).getWidth() / 2) - 5, y - (int) (characterName.getStringSize(abstractPlayerObject.getName()).getHeight() / 2) - 5, (int) characterName.getStringSize(abstractPlayerObject.getName()).getWidth() + 10, (int) (characterName.getStringSize(abstractPlayerObject.getName()).getHeight() / 2) + 10, new Color(abstractPlayerObject.getColor().getRed(), abstractPlayerObject.getColor().getGreen(), abstractPlayerObject.getColor().getBlue(), 50));
            renderer.textWithShadow(graphics, abstractPlayerObject.getName(), (int) ((x + healthBarWidth / 2) - characterName.getStringSize(abstractPlayerObject.getName()).getWidth() / 2), y, Color.WHITE, characterName);

            y += 5;

            drawHealthBar(graphics, x, y, healthBarWidth, healthBarHeight, abstractPlayerObject);

            y += healthBarHeight;

            drawLives(graphics, x + healthBarWidth / 4, y, healthBarWidth / 2, 20, 5, 2, itemImage.getWidth(), abstractPlayerObject);
            if(gameObjects.size() > 1) {
                x += ((Game.instance.getSize().width - 100) / (gameObjects.size() - 1) - healthBarWidth / (gameObjects.size() - 1));
            }

        }
    }

    private void drawHealthBar(Graphics graphics, int x, int y, int width, int height, AbstractPlayerObject abstractPlayerObject) {
        final float percentage = (float) ((abstractPlayerObject.getHealthPoints() * 100) / abstractPlayerObject.getBaseHealthPoints());
        renderer.drawFillRectangle(graphics, x, y, width, height, new Color(0, 0, 0, 50));
        renderer.drawFillRectangle(graphics, x + 1, y + 1, (int) ((width - 2) * (percentage / 100)), height - 2, Color.GREEN);
        renderer.textWithShadow(graphics, "HP: " + abstractPlayerObject.getHealthPoints(), x + 4, y + 13, Color.LIGHT_GRAY, new Font("Roboto", Font.PLAIN, 12));
    }

    private void drawLives(Graphics graphics, int x, int y, int width, int height, int borderDistanceX, int borderDistanceY, int heartDistanceX, AbstractPlayerObject abstractPlayerObject) {
        renderer.drawFillRectangle(graphics, x - borderDistanceX / 2, y, width + borderDistanceX, height + borderDistanceY, new Color(abstractPlayerObject.getColor().getRed(), abstractPlayerObject.getColor().getGreen(), abstractPlayerObject.getColor().getBlue(), 50));
        int lifeX = x + borderDistanceX;
        if (abstractPlayerObject.getLives() <= AbstractPlayerObject.INITIAL_LIVES) {
            for (int i = 0; i < AbstractPlayerObject.INITIAL_LIVES; i++) {
                renderer.img(graphics, itemImageGray, lifeX, y + borderDistanceY / 2 - itemImage.getHeight() / 2 + height / 2, itemImage.getWidth(), itemImage.getHeight());
                lifeX += ((width - heartDistanceX) / (AbstractPlayerObject.INITIAL_LIVES - 1) - (itemImage.getWidth() - heartDistanceX + borderDistanceX * 2) / (AbstractPlayerObject.INITIAL_LIVES - 1));
            }
            lifeX = x + borderDistanceX;
        }
        for (int i = 0; i < abstractPlayerObject.getLives(); i++) {
            renderer.img(graphics, itemImage, lifeX, y + borderDistanceY / 2 - itemImage.getHeight() / 2 + height / 2, itemImage.getWidth(), itemImage.getHeight());
            lifeX += ((width - heartDistanceX) / (AbstractPlayerObject.INITIAL_LIVES - 1) - (itemImage.getWidth() - heartDistanceX + borderDistanceX * 2) / (AbstractPlayerObject.INITIAL_LIVES - 1));
        }
    }
}
