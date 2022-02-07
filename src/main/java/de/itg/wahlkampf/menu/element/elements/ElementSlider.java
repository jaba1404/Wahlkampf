package de.itg.wahlkampf.menu.element.elements;

import de.itg.wahlkampf.menu.Panel;
import de.itg.wahlkampf.menu.element.Element;
import de.itg.wahlkampf.setting.Setting;
import de.itg.wahlkampf.setting.settings.SettingSlider;
import de.itg.wahlkampf.utilities.Font;
import de.itg.wahlkampf.utilities.MathHelper;

import java.awt.*;

public class ElementSlider extends Element {
    private final SettingSlider settingSlider;
    private final MathHelper mathHelper;
    private boolean dragging;
    private final de.itg.wahlkampf.utilities.Font textFont = new de.itg.wahlkampf.utilities.Font("Roboto", Font.BOLD, 14);

    public ElementSlider(Panel panel, SettingSlider settingSlider) {
        super(panel);
        mathHelper = new MathHelper();
        this.settingSlider = settingSlider;
    }

    @Override
    public void drawScreen(Graphics graphics, int mouseX, int mouseY) {
        if (dragging) {
            double diff = settingSlider.getMaxValue() - settingSlider.getMinValue();
            double value = diff / (100) * (mouseX - getX()) + settingSlider.getMinValue();
            settingSlider.setCurrentValue(mathHelper.getRounded((float) Math.min(Math.max(value, settingSlider.getMinValue()), settingSlider.getMaxValue()), (float) settingSlider.getAccuracy()));
        }
        final double diff = settingSlider.getMaxValue() - settingSlider.getMinValue();
        final double value = (settingSlider.getCurrentValue() - settingSlider.getMinValue()) * (100) / diff;
        getRenderer().drawFillRectangle(graphics, getX(), getY(), 100, getHeight(), new Color(255, 255, 255, 100));
        getRenderer().drawFillRectangle(graphics, getX(), getY(), (int) value, getHeight(), new Color(230, 230, 230, 200));
        getRenderer().textWithShadow(graphics, settingSlider.getName() + ": " + settingSlider.getCurrentValue(), getX(), getY() + (getHeight() + (int) textFont.getStringSize(settingSlider.getName() + ": " + settingSlider.getCurrentValue()).getHeight() / 2) / 2, new Color(230, 230, 230, 255), textFont);
    }

    @Override
    public void mousePressed(int mouseX, int mouseY, int mouseButton) {
        if (!settingSlider.canRender() || !settingSlider.isShowInOptions())
            return;
        if (mouseX >= getX() && mouseX < getX() + 100 && mouseY >= getY() && mouseY < getY() + getHeight()) {
            if (mouseButton == 1) {
                dragging = true;
            }
        }
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY) {
        if (!settingSlider.canRender() || !settingSlider.isShowInOptions())
            return;
        dragging = false;
    }

    @Override
    public int getHeight() {
        return getSetting().canRender() && getSetting().isShowInOptions() ? textFont.getSize() : 0;
    }

    @Override
    public Setting getSetting() {
        return settingSlider;
    }

}
