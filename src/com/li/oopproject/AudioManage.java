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
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat originalFormat = originalStream.getFormat();

            // Define the standard format: 44.1 kHz, 16 bit, stereo
            AudioFormat standardFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
                    44100.0F,
                    16,
                    2,
                    4,
                    44100.0F,
                    originalFormat.isBigEndian());

            // Check if conversion is needed
            if (!AudioSystem.isConversionSupported(standardFormat, originalFormat)) {
                System.out.println("Conversion not supported.");
                return;
            }

            // Get the converted audio stream
            AudioInputStream convertedStream = AudioSystem.getAudioInputStream(standardFormat, originalStream);

            // Play the audio
            clip = AudioSystem.getClip();
            clip.open(convertedStream);
            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f); // Adjust the volume level as needed
            clip.loop(Clip.LOOP_CONTINUOUSLY);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
