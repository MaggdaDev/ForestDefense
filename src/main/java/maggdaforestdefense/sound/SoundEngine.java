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

    public final static String MUSIC_PATH = "maggdaforestdefense/sound/music/";
    public final static String MUSIC_1_INTRO = "runden_1_intro.wav",
            MUSIC_1_LOOP = "runden_1_loop.wav",
            MUSIC_2 = "runden_2.wav",
            MUSIC_3_INTRO = "runden_3_intro.wav",
            MUSIC_3_LOOP = "runden_3_loop.wav",
            MUSIC_MENU_INTRO = "menu_intro.wav",
            MUSIC_MENU_LOOP = "menu_loop.wav",
            MUSIC_GAMEOVER = "gameover.mp3";

    public final static double DEFAULT_VOLUME = 0.1;

    private MediaPlayer runden1Intro, runden1Loop, runden2, runden3Intro, runden3Loop, menuIntro, menuLoop, gameover;

    private MediaPlayer currentlyPlaying;

    public SoundEngine() {
        loadSoundTracks();

    }

    public void playSound(Sound sound) {

        if (currentlyPlaying != null) {
            currentlyPlaying.stop();
        }
        switch (sound) {
            case GAMEOVER:
                playMedia(gameover);
                currentlyPlaying = gameover;
                break;
            case MENU_INTRO:
                playMedia(menuIntro);
                menuIntro.setOnEndOfMedia(() -> {
                    playSound(Sound.MENU_LOOP);
                });
                currentlyPlaying = menuIntro;
                break;
            case MENU_LOOP:
                playMedia(menuLoop);
                currentlyPlaying = menuLoop;
                break;
            case RUNDEN_1_INTRO:
                playMedia(runden1Intro);
                runden1Intro.setOnEndOfMedia(() -> {
                    playSound(Sound.RUNDEN_1_LOOP);
                });
                currentlyPlaying = runden1Intro;
                break;
            case RUNDEN_1_LOOP:
                playMedia(runden1Loop);
                currentlyPlaying = runden1Loop;
                break;
            case RUNDEN_2:
                playMedia(runden2);
                currentlyPlaying = runden2;

                break;
            case RUNDEN_3_INTRO:
                playMedia(runden3Intro);
                currentlyPlaying = runden3Intro;
                runden3Intro.setOnEndOfMedia(() -> {
                    playSound(Sound.RUNDEN_3_LOOP);
                });
                break;
            case RUNDEN_3_LOOP:
                playMedia(runden3Loop);
                currentlyPlaying = runden3Loop;
                break;
        }

    }

    public void playLater(Sound sound) {
        currentlyPlaying.setOnEndOfMedia(() -> {
            playSound(sound);
        });
    }

    private void playMedia(MediaPlayer media) {
        media.play();
        media.setCycleCount(MediaPlayer.INDEFINITE);
    }

    private final void loadSoundTracks() {
        runden1Intro = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_1_INTRO));
        runden1Intro.setVolume(DEFAULT_VOLUME);

        runden1Loop = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_1_LOOP));
        runden1Loop.setVolume(DEFAULT_VOLUME);

        runden2 = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_2));
        runden2.setVolume(DEFAULT_VOLUME);

        runden3Intro = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_3_INTRO));
        runden3Intro.setVolume(DEFAULT_VOLUME);

        runden3Loop = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_3_LOOP));
        runden3Loop.setVolume(DEFAULT_VOLUME);

        menuIntro = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_MENU_INTRO));
        menuIntro.setVolume(DEFAULT_VOLUME);

        menuLoop = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_MENU_LOOP));
        menuLoop.setVolume(DEFAULT_VOLUME);

        gameover = new MediaPlayer(MediaLoader.loadMedia(MUSIC_PATH + MUSIC_GAMEOVER));
        gameover.setVolume(DEFAULT_VOLUME);
    }

    public static enum Sound {
        RUNDEN_1_INTRO,
        RUNDEN_1_LOOP,
        RUNDEN_2,
        RUNDEN_3_INTRO,
        RUNDEN_3_LOOP,
        MENU_INTRO,
        MENU_LOOP,
        GAMEOVER;
    }

}
