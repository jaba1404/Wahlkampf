package de.itg.wahlkampf.setting;

public abstract class AbstractSetting {

    private final String name;
    private final String settingToShow;
    private boolean showInOptions;

    public AbstractSetting(String name) {
        this.name = name;
        this.showInOptions = true;
        this.settingToShow = null;
    }

    public AbstractSetting(String name, boolean showInOptions) {
        this.name = name;
        this.showInOptions = showInOptions;
        this.settingToShow = null;
    }

    public AbstractSetting(String name, String settingToShow) {
        this.name = name;
        this.showInOptions = true;
        this.settingToShow = settingToShow;
    }

    public AbstractSetting(String name, boolean showInOptions, String settingToShow) {
        this.name = name;
        this.showInOptions = showInOptions;
        this.settingToShow = settingToShow;
    }

    public String getName() {
        return name;
    }

    public String getSettingToShow() {
        return settingToShow;
    }

    public abstract boolean check(boolean needToBeActive, String settingToShow);

    public final boolean canRender() {
        if (settingToShow != null) {
            if (settingToShow.contains("&")) {
                final String[] strings = settingToShow.split("&");
                boolean render = true;
                for (String string : strings) {
                    final boolean needToBeActive = !string.startsWith("!");
                    if (!check(needToBeActive, string.replace("!", ""))) render = false;
                }
                return render;
            } else {
                return check(!settingToShow.startsWith("!"), settingToShow.replace("!", ""));
            }
        }
        return true;
    }

    public AbstractSetting getSetting() {
        return this;
    }

    public boolean isShowInOptions() {
        return showInOptions;
    }

    public void setShowInOptions(boolean showInOptions) {
        this.showInOptions = showInOptions;
    }
}