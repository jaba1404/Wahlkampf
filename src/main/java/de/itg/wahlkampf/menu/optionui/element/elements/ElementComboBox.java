package de.itg.wahlkampf.menu.optionui.element.elements;

import de.itg.wahlkampf.menu.optionui.Panel;
import de.itg.wahlkampf.menu.optionui.element.AbstractElement;
import de.itg.wahlkampf.setting.AbstractSetting;
import de.itg.wahlkampf.setting.settings.SettingComboBox;
import de.itg.wahlkampf.utilities.Font;

import java.awt.*;

public class ElementComboBox extends AbstractElement {
    private final SettingComboBox settingComboBox;
    private boolean extended;
    private final Font textFont = new Font("Roboto", Font.BOLD, 14);
    private final int originalHeight = 10;
    private final int distance = 15;

    public ElementComboBox(Panel panel, SettingComboBox settingComboBox) {
        super(panel);
        this.settingComboBox = settingComboBox;
    }

    @Override
    public void drawScreen(Graphics graphics, int mouseX, int mouseY) {
        int y = getY();
        getRenderer().drawFillRectangle(graphics, getX(), getY(), 100, getHeight(), new Color(255, 255, 255, 100));
        final String name = settingComboBox.getName() + ":";
        getRenderer().textWithShadow(graphics, name, getX(), getY() + (distance + (int) textFont.getStringSize(name).getHeight() / 2) / 2, new Color(230, 230, 230, 255), textFont);
        y += distance;
        if (extended) {
            for (String option : settingComboBox.getOptions()) {
                getRenderer().textWithShadow(graphics, option, (int) (getX() + 100 / 2 - textFont.getStringSize(option).getWidth() / 2), y + (distance + (int) textFont.getStringSize(option).getHeight() / 2) / 2, settingComboBox.getCurrentOption().equals(option) ? new Color(255, 255, 255, 255) : new Color(230, 230, 230, 255), textFont);
                y += distance;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!settingComboBox.canRender() || !settingComboBox.isShowInOptions())
            return;
        if (mouseX >= getX() && mouseX < getX() + 100 && mouseY >= getY() && mouseY < getY() + getHeight()) {
            int y = getY();
            if (mouseY >= y && mouseY < y + originalHeight) {
                extended = !extended;
                return;
            }
            y += distance;
            if (extended) {
                if (mouseButton == 1) {
                    for (String option : settingComboBox.getOptions()) {
                        if (mouseY >= y && mouseY < y + distance) {
                            settingComboBox.setCurrentOption(option);
                            return;
                        }
                        y += distance;
                    }
                }
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
        return settingComboBox.canRender() && settingComboBox.isShowInOptions() ? distance + (extended ? (distance * settingComboBox.getOptions().size()) : 0) : 0;
    }

    @Override
    public AbstractSetting getSetting() {
        return settingComboBox;
    }
}
