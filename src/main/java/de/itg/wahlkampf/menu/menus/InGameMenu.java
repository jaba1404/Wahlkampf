package de.itg.wahlkampf.menu.menus;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.object.AbstractGameObject;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.util.List;

public class InGameMenu implements IMenu {
    private final List<AbstractGameObject> gameObjects;
    private final Renderer renderer;
    private final Font characterName;
    private final Font playerHealth;
    private final Font playerLives;
    private final Font playerName;

    public InGameMenu(List<AbstractGameObject> gameObjects) {
        this.gameObjects = gameObjects;
        renderer = Game.instance.getRenderer();
        characterName = new Font("Roboto", Font.BOLD, 25);
        playerHealth = new Font("Roboto", Font.BOLD, 17);
        playerLives = new Font("Roboto", Font.BOLD, 15);
        playerName = new Font("Roboto", Font.BOLD, 13);
    }

    @Override
    public void drawScreen(Graphics graphics) {
        int x = 50;
        for (AbstractGameObject abstractPlayerObject : gameObjects) {
            int xString = x;
            int y = Game.instance.getHeight() - 20;
            final String healthDisplay = "HP: " + ((AbstractPlayerObject) abstractPlayerObject).getHealthPoints();
            final String lifeDisplay = "Lives: " + ((AbstractPlayerObject) abstractPlayerObject).getLives();
            final String nameDisplay = "Player " + ((AbstractPlayerObject) abstractPlayerObject).getId();
            renderer.textWithShadow(graphics, abstractPlayerObject.getName(), xString, y, Color.WHITE, characterName);
            y -= characterName.getStringSize(abstractPlayerObject.getName()).getHeight() / 2 + playerHealth.getStringSize(healthDisplay).getHeight() / 2;
            xString += (int) (characterName.getStringSize(abstractPlayerObject.getName()).getWidth() - playerHealth.getStringSize(healthDisplay).getWidth()) / 2;
            renderer.textWithShadow(graphics, healthDisplay, xString, y, Color.WHITE, playerHealth);
            y -= playerHealth.getStringSize(healthDisplay).getHeight() / 2 + playerLives.getStringSize(lifeDisplay).getHeight() / 2;
            xString += (int) (playerHealth.getStringSize(healthDisplay).getWidth() - playerLives.getStringSize(lifeDisplay).getWidth()) / 2;
            renderer.textWithShadow(graphics, lifeDisplay, xString, y, Color.WHITE, playerLives);
            xString += (int) (playerLives.getStringSize(lifeDisplay).getWidth() - playerName.getStringSize(nameDisplay).getWidth()) / 2;
            y -= playerLives.getStringSize(lifeDisplay).getHeight() / 2 + playerName.getStringSize(nameDisplay).getHeight() / 2;
            renderer.textWithShadow(graphics, nameDisplay, xString, y, Color.WHITE, playerName);

            x += (Game.instance.getSize().width / (gameObjects.size() - 1) - 100 - characterName.getStringSize(abstractPlayerObject.getName()).getWidth());
        }
    }

    public List<AbstractGameObject> getGameObjects() {
        return gameObjects;
    }
}
