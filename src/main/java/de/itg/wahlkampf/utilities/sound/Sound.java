package de.itg.wahlkampf.utilities.sound;

import de.itg.wahlkampf.Game;

import java.net.URL;

public enum Sound {

    HIT(Game.class.getResource("assets/Hit_Hurt7.wav")),
    FINISHED(Game.class.getResource("assets/Powerup11.wav")),
    JUMP(Game.class.getResource("assets/Jump9.wav")),
    BLOCK(Game.class.getResource("assets/Block.wav")),
    SETTINGS_CHANGE(Game.class.getResource("assets/SettingsChange.wav"));

    private final URL location;

    Sound(URL location) {
        this.location = location;
    }

    public URL getLocation() {
        return location;
    }
}
