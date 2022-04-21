package de.itg.wahlkampf.menu.optionui.element;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.menu.menus.IMenu;
import de.itg.wahlkampf.menu.optionui.IComponent;
import de.itg.wahlkampf.menu.optionui.Panel;
import de.itg.wahlkampf.setting.AbstractSetting;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;

public abstract class AbstractElement {
    private final Renderer renderer;
    private final Panel panel;
    private int x, y;

    public AbstractElement(Panel panel) {
        this.panel = panel;
        renderer = Game.instance.getRenderer();
    }

    public void drawScreen(Graphics graphics, int x, int y, int mouseX, int mouseY) {
        if (!getSetting().canRender() || !getSetting().isShowInOptions())
            return;
        this.x = x;
        this.y = y;
        drawScreen(graphics, mouseX, mouseY);
    }

    public abstract void drawScreen(Graphics graphics, int mouseX, int mouseY);

    public abstract void mouseClicked(int mouseX, int mouseY, int mouseButton);

    public abstract void mousePressed(int mouseX, int mouseY, int mouseButton);

    public abstract void mouseReleased(int mouseX, int mouseY);


    public abstract int getHeight();

    public abstract AbstractSetting getSetting();

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return 200;
    }

    public Renderer getRenderer() {
        return renderer;
    }
}
