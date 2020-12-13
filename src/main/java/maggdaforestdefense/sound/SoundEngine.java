/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.sound;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import maggdaforestdefense.storage.MediaLoader;

/**
 *
 * @author DavidPrivat
 */
public class SoundEngine {
    public final static String SOUNDTRACK_PATH = "maggdaforestdefense/sound/music/soundtrack.mp3";
    
    public final static double DEFAULT_VOLUME = 0.2;
    
    private MediaPlayer soundtrack;
    
    private MediaPlayer mediaPlayer;
    
    public SoundEngine() {
        loadSoundTracks();
        
    }
    
    public void playSound(Sound sound) {
        switch(sound) {
            case SOUNDTRACK:
                playMedia(soundtrack);
                break;
        }
    }
    
    private void playMedia(MediaPlayer media) {
        media.play();
        media.setCycleCount(MediaPlayer.INDEFINITE);
    }
    
    private final void loadSoundTracks() {
        soundtrack = new MediaPlayer(MediaLoader.loadMedia(SOUNDTRACK_PATH));
        soundtrack.setVolume(DEFAULT_VOLUME);
    }
    
    public static enum Sound {
        SOUNDTRACK;
    }
    
}
