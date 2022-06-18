package de.itg.wahlkampf.setting;

import de.itg.wahlkampf.Game;
import de.itg.wahlkampf.setting.settings.SettingCheckBox;
import de.itg.wahlkampf.setting.settings.SettingComboBox;
import de.itg.wahlkampf.setting.settings.SettingSlider;

import java.util.ArrayList;
import java.util.List;

public class SettingManager {
    private final List<AbstractSetting> settingList = new ArrayList<>();

    public SettingManager() {
        settingList.add(new SettingCheckBox("Start Game", false, false));
        settingList.add(new SettingComboBox("Stage", Game.instance.getBackgroundMap().keySet().toArray(new String[0]), Game.instance.getBackgroundMap().keySet().toArray(new String[0])[0], true));
        for (int i = 0; i < Game.MAX_PLAYER_AMOUNT; i++) {
            settingList.add(new SettingComboBox("Player " + i, Game.instance.getPlayerNames().toArray(new String[0]), Game.instance.getPlayerNames().get(0), true));
        }
        settingList.add(new SettingSlider("Knockback Modifier X", 0,50,19,1));
        settingList.add(new SettingSlider("Knockback Modifier Y", 0,50,15,1));
        settingList.add(new SettingCheckBox("Show FPS",false));
    }

    public AbstractSetting getSettingByName(String name) {
        return settingList.stream().filter(setting -> setting.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<AbstractSetting> getSettingList() {
        return settingList;
    }
}
