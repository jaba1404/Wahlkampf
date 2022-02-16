package de.itg.wahlkampf.menu.menus;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.util.List;

public class InGameMenu {
    private final List<AbstractGameObject> gameObjects;
    private final Renderer renderer;
    private final Font playerName;
    private final Font playerHealth;
    private final Font playerLives;

    public InGameMenu(List<AbstractGameObject> gameObjects) {
        this.gameObjects = gameObjects;
        renderer = Game.instance.getRenderer();
        playerName = new Font("Roboto", Font.BOLD, 30);
        playerHealth = new Font("Roboto", Font.BOLD, 16);
        playerLives = new Font("Roboto", Font.BOLD, 12);
    }


    public void drawScreen(Graphics graphics) {
        int x = 50;
        for (AbstractGameObject abstractPlayerObject : gameObjects) {
            int y = Game.instance.getHeight() - 20;
            final String healthDisplay = "HP: " + ((AbstractPlayerObject) abstractPlayerObject).getHealthPoints();
            final String lifeDisplay = "Lives: " + ((AbstractPlayerObject) abstractPlayerObject).getLives();

            renderer.textWithShadow(graphics, abstractPlayerObject.getName(), x, y, Color.WHITE, playerName);
            y -= playerName.getStringSize(abstractPlayerObject.getName()).getHeight() / 2 + playerHealth.getStringSize(healthDisplay).getHeight() / 2;
            renderer.textWithShadow(graphics, healthDisplay, (int) (x + (playerName.getStringSize(abstractPlayerObject.getName()).getWidth() - playerHealth.getStringSize(healthDisplay).getWidth()) / 2), y, Color.WHITE, playerHealth);
            y -= playerHealth.getStringSize(healthDisplay).getHeight() / 2 + playerLives.getStringSize(lifeDisplay).getHeight() / 2;
            renderer.textWithShadow(graphics, lifeDisplay, (int) (x + (playerHealth.getStringSize(healthDisplay).getWidth() - playerLives.getStringSize(lifeDisplay).getWidth()) / 2), y, Color.WHITE, playerLives);
            x += (Game.instance.getSize().width / (gameObjects.size() - 1) - 100 - playerName.getStringSize(abstractPlayerObject.getName()).getWidth());
        }
    }

    public List<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }
}
