/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javafx.scene.image.Image;

/**
 *
 * @author David
 */
public enum GameImage {

    TEST("test/test.bmp");

    private Image image;

    GameImage(String path) {
        try {
            image = new Image(GameImage.class.getClassLoader().getResource(path).toString());
        } catch(Exception e) {
            image = null;
            e.printStackTrace();
        }
                
    }
    public Image getImage() {

        return image;
    }
}
