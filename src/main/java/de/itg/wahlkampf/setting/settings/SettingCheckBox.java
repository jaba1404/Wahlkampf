package de.itg.wahlkampf.setting.settings;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.SettingChangeEvent;
import de.itg.wahlkampf.setting.AbstractSetting;

public class SettingCheckBox extends AbstractSetting {
    private boolean active;

    public SettingCheckBox(String name, boolean showInOptions, boolean active) {
        super(name, showInOptions);
        this.active = active;
    }

    public SettingCheckBox(String name, boolean showInOptions, boolean active, String settingToShow) {
        super(name, showInOptions, settingToShow);
        this.active = active;
    }

    public SettingCheckBox(String name, boolean active) {
        super(name);
        this.active = active;
    }

    public SettingCheckBox(String name, boolean active, String settingToShow) {
        super(name, settingToShow);
        this.active = active;
    }

    @Override
    public boolean check(boolean needToBeActive, String settingToShow) {
        final AbstractSetting settingSimple = Game.instance.getSettingManager().getSettingByName(settingToShow);
        if (settingSimple instanceof final SettingCheckBox settingCheckBox) {
            return needToBeActive ? settingCheckBox.isActive() && settingSimple.canRender() : !settingCheckBox.isActive();
        }
        return true;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        final SettingChangeEvent event = new SettingChangeEvent(this, this.active, active);
        Game.instance.onEvent(event);
    }
}
