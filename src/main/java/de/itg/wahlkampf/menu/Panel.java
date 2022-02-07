package de.itg.wahlkampf.menu;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.menu.element.Element;
import de.itg.wahlkampf.menu.element.elements.ElementCheckBox;
import de.itg.wahlkampf.menu.element.elements.ElementComboBox;
import de.itg.wahlkampf.menu.element.elements.ElementSlider;
import de.itg.wahlkampf.setting.settings.SettingCheckBox;
import de.itg.wahlkampf.setting.settings.SettingComboBox;
import de.itg.wahlkampf.setting.settings.SettingSlider;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.Renderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Panel {
    private String title;
    private int x, y, clickX, clickY;
    private boolean dragging, extended;
    private final Font panelFont = new Font("Roboto", Font.BOLD, 16);
    private final Renderer renderer;
    private final List<Element> elements = new ArrayList<>();
    private int width = 200;
    private int height = 20;
    private boolean isVisible;

    public Panel(String title, int x, int y) {
        this.renderer = Game.instance.getRenderer();
        this.title = title;
        this.x = x;
        this.y = y;
        loadElements();
    }

    public void drawScreen(Graphics graphics, int mouseX, int mouseY) {
        if(!isVisible)
            return;
        if (dragging) {
            x = clickX + mouseX;
            y = clickY + mouseY;
        }
        renderer.drawFillRectangle(graphics, getX(), getY(), getWidth(), getHeight(), new Color(255, 255, 255, 100));
        renderer.textWithShadow(graphics, getTitle(), getX(), getY() + getHeight() - (int) panelFont.getStringSize(getTitle()).getHeight() / 3, new Color(230, 230, 230, 255), panelFont);
        if (extended) {
            renderer.drawFillRectangle(graphics, getX(), y + getHeight() - 1, getWidth(), 1, Color.WHITE);
            int y = getY() + getHeight();
            for (Element element : elements) {
                element.drawScreen(graphics, getX(), y, mouseX, mouseY);
                y += element.getHeight();
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(!isVisible)
            return;
        if (mouseX >= x && mouseX < x + getWidth() && mouseY >= y && mouseY < y + getHeight()) {
            if (mouseButton == 3) {
                extended = !extended;
            }
        }
        if (extended) {
            for (Element element : elements) {
                element.mouseClicked(mouseX, mouseY, mouseButton);
            }
        }
    }

    public void mouseDragged(int mouseX, int mouseY, int mouseButton) {
        if(!isVisible)
            return;
    }

    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (!isVisible)
            return;
        if (extended) {
            for (Element element : elements) {
                element.mousePressed(mouseX, mouseY, mouseButton);
            }
        }
        if (mouseX >= x && mouseX < x + getWidth() && mouseY >= y && mouseY < y + getHeight()) {
            if (mouseButton == 1) {

                clickX = x - mouseX;
                clickY = y - mouseY;
                dragging = true;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY) {
        if(!isVisible)
            return;
        dragging = false;
        if (extended) {
            for (Element element : elements) {
                element.mouseReleased(mouseX, mouseY);
            }
        }
    }

    private void loadElements() {
        elements.clear();
        Game.instance.getSettingManager().getSettingList().forEach(setting -> {
            if (setting instanceof SettingCheckBox) {
                elements.add(new ElementCheckBox(this, (SettingCheckBox) setting));
            }
            if (setting instanceof SettingSlider) {
                elements.add(new ElementSlider(this, (SettingSlider) setting));
            }
            if (setting instanceof SettingComboBox) {
                elements.add(new ElementComboBox(this, (SettingComboBox) setting));
            }
        });
    }

    public String getTitle() {
        return title;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    protected int getHeight() {
        return height;
    }

    protected int getWidth() {
        return width;
    }

    private void setHeight(int height) {
        this.height = height;
    }

    private void setWidth(int width) {
        this.width = width;
    }

    public boolean isDragging() {
        return dragging;
    }

    public boolean isExtended() {
        return extended;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
