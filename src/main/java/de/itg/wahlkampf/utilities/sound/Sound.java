package de.itg.wahlkampf.utilities.sound;

import java.io.File;

public enum Sound {
    HIT(new File("resources/Hit_Hurt7.wav")),
    FINISHED(new File("resources/Powerup11.wav")),
    JUMP(new File("resources/Jump9.wav")),
    BLOCK(new File("resources/Block.wav")),
    SETTINGS_CHANGE(new File("resources/SettingsChange.wav"));

    private File file;

    Sound(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }
}
