package de.itg.wahlkampf.menu;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.setting.settings.SettingCheckBox;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Menu extends MouseAdapter {
    private final Button startButton;
    private final Button optionButton;
    private final Button quitButton;
    private int mouseX, mouseY;
    private final Panel panel;
    private final SettingCheckBox startGame;
    private final int width = 200;
    private final int height = 50;
    private final Font titleFont;
    private final Font versionFont;
    private final Renderer renderer;

    public Menu() {
        final int x = (Game.instance.getSize().width - width) / 2;
        final int centerY = (Game.instance.getSize().height - height) / 2;
        startButton = new Button("StartButton", "Start Game", x, centerY - 60, width, height, 30, true, true);
        optionButton = new Button("OptionButton", "Options", x, centerY, width, height, 30, true, true);
        quitButton = new Button("QuitButton", "Quit Game", x, centerY + 60, width, height, 30, true, true);
        startGame = (SettingCheckBox) Game.instance.getSettingManager().getSettingByName("Start Game");
        panel = new Panel("Options", 50, 300);
        titleFont = new Font("Roboto", Font.BOLD, 64);
        versionFont = new Font("Roboto", Font.BOLD, 16);
        renderer = Game.instance.getRenderer();
    }

    public void drawScreen(Graphics graphics) {
        if (startGame.isActive())
            return;
        startButton.drawScreen(graphics, mouseX, mouseY);
        optionButton.drawScreen(graphics, mouseX, mouseY);
        quitButton.drawScreen(graphics, mouseX, mouseY);
        panel.drawScreen(graphics, mouseX, mouseY);
        renderer.textWithShadow(graphics, Game.GAME_TITLE, (int) ((Game.instance.getSize().width - titleFont.getStringSize(Game.GAME_TITLE).getWidth()) / 2), Game.instance.getSize().height / 2 - 120, new Color(230, 230, 230, 255), titleFont);
        renderer.textWithShadow(graphics, Game.GAME_VERSION, (int) ((Game.instance.getSize().width - versionFont.getStringSize(Game.GAME_VERSION).getWidth()) / 2), Game.instance.getSize().height / 2 - 105, new Color(230, 230, 230, 255), versionFont);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (startGame.isActive())
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mouseClicked(mouseX, mouseY, e.getButton());
        if (startButton.canClick(mouseX, mouseY)) {
            startGame.setActive(true);
        }
        if (optionButton.canClick(mouseX, mouseY)) {
            panel.setVisible(!panel.isVisible());
        }
        if (quitButton.canClick(mouseX, mouseY)) {
            System.exit(0);
        }
        super.mouseClicked(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (startGame.isActive())
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mouseDragged(mouseX, mouseY, e.getButton());

        super.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (startGame.isActive())
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mousePressed(mouseX, mouseY, e.getButton());

        super.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (startGame.isActive())
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mouseReleased(mouseX, mouseY);

        super.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (startGame.isActive())
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        super.mouseMoved(e);
    }
}

