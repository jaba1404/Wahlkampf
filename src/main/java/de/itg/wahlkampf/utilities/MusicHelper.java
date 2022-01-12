package de.itg.wahlkampf.utilities;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicHelper {
    public void playSound(String path) throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        AudioInputStream audioIn = AudioSystem.getAudioInputStream(new File(path));
        Clip clip = AudioSystem.getClip();
        clip.open(audioIn);
        clip.start();
    }
    public enum Music {
        HIT("resources/Hit_Hurt7.wav");
        private String path;

        Music(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }

}
