package de.itg.wahlkampf.menu.element;

import de.itg.wahlkampf.menu.Panel;
import de.itg.wahlkampf.setting.Setting;

import java.awt.*;

public abstract class Element {
    private final Panel panel;
    private int x,y;

    public Element(Panel panel) {
        this.panel = panel;
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

    public abstract void mouseReleased(int mouseX, int mouseY);

    public abstract int getHeight();

    public abstract Setting getSetting();

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
}
