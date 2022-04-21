package de.itg.wahlkampf.menu.optionui.element.elements;

import de.itg.wahlkampf.menu.optionui.Panel;
import de.itg.wahlkampf.menu.optionui.element.AbstractElement;
import de.itg.wahlkampf.setting.settings.SettingCheckBox;
import de.itg.wahlkampf.utilities.Font;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class ElementCheckBox extends AbstractElement {
    private final SettingCheckBox setting;
    private final Font textFont = new Font("Roboto", Font.BOLD, 14);

    public ElementCheckBox(Panel panel, SettingCheckBox setting) {
        super(panel);
        this.setting = setting;
    }

    @Override
    public void drawScreen(Graphics graphics, int mouseX, int mouseY) {
        getRenderer().drawFillRectangle(graphics, getX(), getY(), getWidth(), getHeight(), new Color(255, 255, 255, 100));
        final String settingName = getSetting().getName() + ": ";
        final Rectangle2D fontSize = textFont.getStringSize(settingName);
        getRenderer().textWithShadow(graphics, settingName + setting.isActive(), getX(), getY() + (getHeight() + (int) fontSize.getHeight() / 2) / 2, new Color(220, 220, 220, 255), textFont);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!setting.canRender() || !getSetting().isShowInOptions())
            return;
        if (mouseButton == 1) {
            if (mouseX <= getX() + getWidth() && mouseX > getX() && mouseY <= getY() + getHeight() && mouseY > getY()) {
                setting.setActive(!setting.isActive());
            }
        }
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {

    }

    @Override
    public int getHeight() {
        return getSetting().canRender() && getSetting().isShowInOptions() ? 15 : 0;
    }

    @Override
    public SettingCheckBox getSetting() {
        return setting;
    }
}
