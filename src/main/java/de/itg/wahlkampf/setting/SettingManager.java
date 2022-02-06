package de.itg.wahlkampf.setting;

import de.itg.wahlkampf.setting.settings.SettingCheckBox;

import java.util.ArrayList;
import java.util.List;

public class SettingManager {
    private final List<Setting> settingList = new ArrayList<>();

    public SettingManager() {
        settingList.add(new SettingCheckBox("Test 1", false));
        settingList.add(new SettingCheckBox("Start Game", false,false));
        settingList.add(new SettingCheckBox("Pause Game", false,false));
        settingList.add(new SettingCheckBox("Test 2", true));

    }

    public Setting getSettingByName(String name) {
        return settingList.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Setting> getSettingList() {
        return settingList;
    }
}
