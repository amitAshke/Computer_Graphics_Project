package Player;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.Random;

/**
 * This class represents the sound effects player.
 */
public class SoundPlayer {
    private File[] standardSounds = new File[2];
    private File specialSound;

    public SoundPlayer() {
        standardSounds[0] = new File("src\\resources\\sfx\\StandardActivation1.wav");
        standardSounds[1] = new File("src\\resources\\sfx\\StandardActivation2.wav");

        specialSound = new File("src\\resources\\sfx\\SpecialActivation.wav");
    }

    /**
     * Plays one of the sound effects for the standard projectile.
     */
    public void playStandard() {
        try {
            Random rand = new Random();
            int randomIndex = rand.nextInt(standardSounds.length);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(standardSounds[randomIndex]));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Plays the sound effect of the special projectile.
     */
    public void playSpecial() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(specialSound));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
