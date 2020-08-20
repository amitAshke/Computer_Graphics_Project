package Main;

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
    private File[] dummyHitSound = new File[2];
    private File dummyKillSound;
    private File[] playerHitSound = new File[4];
    private File[] playerDieSound = new File[4];

    public SoundPlayer() {
        standardSounds[0] = new File("src\\resources\\sfx\\StandardActivation1.wav");
        standardSounds[1] = new File("src\\resources\\sfx\\StandardActivation2.wav");
        specialSound = new File("src\\resources\\sfx\\SpecialActivation.wav");

        dummyHitSound[0] = new File("src\\resources\\sfx\\HitSound1.wav");
        dummyHitSound[1] = new File("src\\resources\\sfx\\HitSound2.wav");
        dummyKillSound = new File("src\\resources\\sfx\\KillSound.wav");

        playerHitSound[0] = new File("src\\resources\\sfx\\PlayerHit1.wav");
        playerHitSound[1] = new File("src\\resources\\sfx\\PlayerHit2.wav");
        playerHitSound[2] = new File("src\\resources\\sfx\\PlayerHit3.wav");
        playerHitSound[3] = new File("src\\resources\\sfx\\PlayerHit4.wav");
        playerDieSound[0] = new File("src\\resources\\sfx\\PlayerDie1.wav");
        playerDieSound[1] = new File("src\\resources\\sfx\\PlayerDie2.wav");
        playerDieSound[2] = new File("src\\resources\\sfx\\PlayerDie3.wav");
        playerDieSound[3] = new File("src\\resources\\sfx\\PlayerDie4.wav");
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

    /**
     * Plays the sound effect of an enemy hit.
     */
    public void playDummyHit() {
        try {
            Random rand = new Random();
            int randomIndex = rand.nextInt(dummyHitSound.length);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(dummyHitSound[randomIndex]));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * Plays the sound effect of an enemy kill.
     */
    public void playDummyKill() {
        try {
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(dummyKillSound));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void playPlayerHit() {
        try {
            Random rand = new Random();
            int randomIndex = rand.nextInt(playerHitSound.length);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(playerHitSound[randomIndex]));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void playPlayerDie() {
        try {
            Random rand = new Random();
            int randomIndex = rand.nextInt(playerDieSound.length);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(playerDieSound[randomIndex]));
            clip.start();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
