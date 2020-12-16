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
    public final static String RUNDENMUSIK_1_PATH = "maggdaforestdefense/sound/music/rundenmusik1.mp3",
            RUNDENMUSIK_2_PATH = "maggdaforestdefense/sound/music/rundenmusik2.mp3",
            GAMEOVER_MUSIK_PATH = "maggdaforestdefense/sound/music/gameover.mp3",
            MENUMUSIK_PATH = "maggdaforestdefense/sound/music/menumusik.mp3";
    
    public final static double DEFAULT_VOLUME = 0.05;
    
    private MediaPlayer rundenmusik1, rundenmusik2, gameoverMusik, menuMusik;
    
    private MediaPlayer currentlyPlaying;

    
    public SoundEngine() {
        loadSoundTracks();
        
    }
    
    public void playSound(Sound sound) {
        

        if(currentlyPlaying != null) {
            currentlyPlaying.stop();
        }
        switch(sound) {
            case RUNDENMUSIK_1:
                playMedia(rundenmusik1);
                currentlyPlaying = rundenmusik1;
                break;
            case RUNDUNMUSIK_2:
                playMedia(rundenmusik2);
                currentlyPlaying = rundenmusik2;
                break;
            case GAMEOVER_MUSIK:
                playMedia(gameoverMusik);
                currentlyPlaying = gameoverMusik;
                break;
            case MENUMUSIK:
                playMedia(menuMusik);
                currentlyPlaying = menuMusik;
                break;
        }

    }
    
    private void playMedia(MediaPlayer media) {
        media.play();
        media.setCycleCount(MediaPlayer.INDEFINITE);
    }
    
    private final void loadSoundTracks() {
        rundenmusik1 = new MediaPlayer(MediaLoader.loadMedia(RUNDENMUSIK_1_PATH));
        rundenmusik1.setVolume(DEFAULT_VOLUME);
        
        rundenmusik2 = new MediaPlayer(MediaLoader.loadMedia(RUNDENMUSIK_2_PATH));
        rundenmusik2.setVolume(DEFAULT_VOLUME);
        
        gameoverMusik = new MediaPlayer(MediaLoader.loadMedia(GAMEOVER_MUSIK_PATH));
        gameoverMusik.setVolume(DEFAULT_VOLUME);
        
        menuMusik = new MediaPlayer(MediaLoader.loadMedia(MENUMUSIK_PATH));
        menuMusik.setVolume(DEFAULT_VOLUME);
    }
    
    public static enum Sound {
        RUNDENMUSIK_1,
        RUNDUNMUSIK_2,
        MENUMUSIK,
        GAMEOVER_MUSIK;
    }
    
}
