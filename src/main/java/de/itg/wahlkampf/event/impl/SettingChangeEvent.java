package de.itg.wahlkampf.event.impl;

import de.itg.wahlkampf.event.Event;
import de.itg.wahlkampf.setting.AbstractSetting;

public class SettingChangeEvent extends Event {

    private final AbstractSetting target;
    private boolean srcBoolean, dstBoolean;
    private float srcFloat, dstFloat;
    private String srcString, dstString;

    public SettingChangeEvent(AbstractSetting target) {
        this.target = target;
    }

    public SettingChangeEvent(AbstractSetting target, boolean srcBoolean, boolean dstBoolean) {
        this.target = target;
        this.srcBoolean = srcBoolean;
        this.dstBoolean = dstBoolean;
    }

    public SettingChangeEvent(AbstractSetting target, float srcFloat, float dstFloat) {
        this.target = target;
        this.srcFloat = srcFloat;
        this.dstFloat = dstFloat;
    }

    public SettingChangeEvent(AbstractSetting target, String srcString, String dstString) {
        this.target = target;
        this.srcString = srcString;
        this.dstString = dstString;
    }

    public AbstractSetting getTarget() {
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