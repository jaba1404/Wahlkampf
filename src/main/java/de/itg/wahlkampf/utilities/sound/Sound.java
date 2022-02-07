package de.itg.wahlkampf.utilities.sound;

public enum Sound {
    HIT("resources/Hit_Hurt7.wav");
    private String path;

    Sound(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
