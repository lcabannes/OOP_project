package com.li.oopproject;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManage {
    private String path = "Audio";
    private String file = "DarkDescent.wav";
    private Clip clip;

    public void loadBGMusic() {
        try {
            File audioFile = new File(path + File.separator + file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    // Later for mute button
    public void stopBGMusic() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
