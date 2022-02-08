package de.itg.wahlkampf.setting.settings;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.setting.AbstractSetting;

public class SettingSlider extends AbstractSetting {
    private final double minValue, maxValue, accuracy;
    private double currentValue;

    public SettingSlider(String name, double minValue, double maxValue, double currentValue, double accuracy) {
        super(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.accuracy = accuracy;
    }

    public SettingSlider(String name, double minValue, double maxValue, double currentValue, double accuracy, boolean showInOptions) {
        super(name, showInOptions);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.accuracy = accuracy;
    }

    public SettingSlider(String name, double minValue, double maxValue, double currentValue, double accuracy, String settingToShow) {
        super(name, settingToShow);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.accuracy = accuracy;
    }

    public SettingSlider(String name, double minValue, double maxValue, double currentValue, double accuracy, boolean showInOptions, String settingToShow) {
        super(name, showInOptions, settingToShow);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.accuracy = accuracy;
    }

    @Override
    public boolean check(boolean needToBeActive, String settingToShow) {
        final AbstractSetting settingSimple = Game.instance.getSettingManager().getSettingByName(settingToShow);
        if (settingSimple instanceof final SettingCheckBox settingCheckBox) {
            return needToBeActive ? settingCheckBox.isActive() && settingSimple.canRender() : !settingCheckBox.isActive();
        }
        return true;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }
}
