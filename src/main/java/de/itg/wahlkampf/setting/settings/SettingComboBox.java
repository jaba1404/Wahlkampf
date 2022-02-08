package de.itg.wahlkampf.setting.settings;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.event.impl.SettingChangeEvent;
import de.itg.wahlkampf.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingComboBox extends Setting {

    private final List<String> options = new ArrayList<>();
    private String currentOption;

    public SettingComboBox(String name, String[] options, String currentOption, boolean showInOptions) {
        super(name, showInOptions);
        this.options.addAll(Arrays.asList(options));
        this.currentOption = currentOption;
    }

    public SettingComboBox(String name, String[] options, String currentOption, String settingToShow) {
        super(name, settingToShow);
        this.options.addAll(Arrays.asList(options));
        this.currentOption = currentOption;
    }

    public SettingComboBox(String name, String[] options, String currentOption, boolean showInOptions, String settingToShow) {
        super(name, showInOptions, settingToShow);
        this.options.addAll(Arrays.asList(options));
        this.currentOption = currentOption;
    }

    @Override
    public boolean check(boolean needToBeActive, String settingToShow) {
        final Setting settingSimple = Game.instance.getSettingManager().getSettingByName(settingToShow);
        if (settingSimple instanceof final SettingCheckBox settingCheckBox) {
            return needToBeActive ? settingCheckBox.isActive() && settingSimple.canRender() : !settingCheckBox.isActive();
        } else {
            for (Setting setting : Game.instance.getSettingManager().getSettingList()) {
                if (setting instanceof SettingComboBox settingComboBox) {
                    if (settingComboBox.optionsContains(settingToShow)) {
                        boolean show = settingComboBox.getCurrentOption().equalsIgnoreCase(settingToShow);
                        return needToBeActive == show;
                    }
                }
            }
        }
        return true;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCurrentOption() {
        return currentOption;
    }

    public void setCurrentOption(String currentOption) {
        if (!currentOption.equalsIgnoreCase(this.currentOption)) {
           final SettingChangeEvent event = new SettingChangeEvent(this, this.currentOption, currentOption);
            Game.instance.onEvent(event);
            if (!event.isCancelled()) {
                this.currentOption = currentOption;
            }
        }
    }

    private boolean optionsContains(String s) {
        for (String s1 : getOptions())
            if (s1.equalsIgnoreCase(s))
                return true;
        return false;
    }
}
