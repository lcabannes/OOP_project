package com.li.oopproject;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioManage {
    private String path = "Audio";
    private String file = "scifi.wav";
    private Clip clip;

    public void loadBGMusic() {
        try {
            File audioFile = new File(path + File.separator + file);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            // Get the FloatControl for volume
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);

            // Set the volume level (in decibels)
            gainControl.setValue(-20.0f); // Adjust the volume level as needed

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
