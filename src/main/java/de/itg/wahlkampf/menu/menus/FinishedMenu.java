package de.itg.wahlkampf.menu.menus;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.menu.Button;
import de.itg.wahlkampf.menu.Panel;
import de.itg.wahlkampf.object.AbstractPlayerObject;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FinishedMenu extends MouseAdapter {
    private final Button quitButton;
    private int mouseX, mouseY;
    private final Panel panel;
    private final int width = 200;
    private final int height = 50;
    private boolean isVisible;
    private final Font titleFont;
    private AbstractPlayerObject abstractPlayerObject;
    private final Renderer renderer;
    final ArrayList<String> players = new ArrayList<>();

    public FinishedMenu() {
        final int x = (Game.instance.getSize().width - width) / 2;
        final int centerY = (Game.instance.getSize().height - height) / 2;

        quitButton = new de.itg.wahlkampf.menu.Button("QuitButton", "Quit Game", x, centerY, width, height, 30, true, true);
        panel = new Panel("Options", 50, 300);
        titleFont = new Font("Roboto", Font.BOLD, 32);
        renderer = Game.instance.getRenderer();
    }

    public void drawScreen(Graphics graphics) {
        if (!isVisible)
            return;

        quitButton.drawScreen(graphics, mouseX, mouseY);
        panel.drawScreen(graphics, mouseX, mouseY);
        final String text = "Dear Mr./Mrs. " + abstractPlayerObject.getName() + ", you have just won the election";
        renderer.textWithShadow(graphics, text, (int) ((Game.instance.getSize().width - titleFont.getStringSize(text).getWidth()) / 2), Game.instance.getSize().height / 2 - 60, new Color(230, 230, 230, 255), titleFont);
        //renderer.textWithShadow(graphics, "You have won!", (int) ((Game.instance.getSize().width - versionFont.getStringSize("You have won!").getWidth()) / 2), Game.instance.getSize().height / 2 - 105, new Color(230, 230, 230, 255), versionFont);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!isVisible)
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mouseClicked(mouseX, mouseY, e.getButton());
        if (quitButton.canClick(mouseX, mouseY)) {
            System.exit(0);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!isVisible)
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mouseDragged(mouseX, mouseY, e.getButton());
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!isVisible)
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mousePressed(mouseX, mouseY, e.getButton());
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isVisible)
            return;
        mouseX = e.getX();
        mouseY = e.getY();
        panel.mouseReleased(mouseX, mouseY);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!isVisible)
            return;
        mouseX = e.getX();
        mouseY = e.getY();
    }

    public AbstractPlayerObject getAbstractPlayerObject() {
        return abstractPlayerObject;
    }

    public void setAbstractPlayerObject(AbstractPlayerObject abstractPlayerObject) {
        this.abstractPlayerObject = abstractPlayerObject;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}

