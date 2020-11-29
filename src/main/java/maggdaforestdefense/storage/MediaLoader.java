/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.storage;

import javafx.scene.media.Media;

/**
 *
 * @author DavidPrivat
 */
public class MediaLoader {
    public static Media loadMedia(String s) {
        return new Media(MediaLoader.class.getClassLoader().getResource(s).toString());
    }
}
