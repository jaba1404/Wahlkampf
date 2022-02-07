package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.Event;
import de.itg.wahlkampf.setting.Setting;

public class SettingChangeEvent extends Event {

    private final Setting target;
    private boolean srcBoolean, dstBoolean;
    private float srcFloat, dstFloat;
    private String srcString, dstString;

    public SettingChangeEvent(Setting target) {
        this.target = target;
    }

    public SettingChangeEvent(Setting target, boolean srcBoolean, boolean dstBoolean) {
        this.target = target;
        this.srcBoolean = srcBoolean;
        this.dstBoolean = dstBoolean;
    }

    public SettingChangeEvent(Setting target, float srcFloat, float dstFloat) {
        this.target = target;
        this.srcFloat = srcFloat;
        this.dstFloat = dstFloat;
    }

    public SettingChangeEvent(Setting target, String srcString, String dstString) {
        this.target = target;
        this.srcString = srcString;
        this.dstString = dstString;
    }

    public Setting getTarget() {
        return target;
    }

    public boolean isSrcBoolean() {
        return srcBoolean;
    }

    public boolean isDstBoolean() {
        return dstBoolean;
    }

    public float getSrcFloat() {
        return srcFloat;
    }

    public float getDstFloat() {
        return dstFloat;
    }

    public String getSrcString() {
        return srcString;
    }

    public String getDstString() {
        return dstString;
    }
}