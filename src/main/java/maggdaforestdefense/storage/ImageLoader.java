/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.storage;

import javafx.scene.image.ImageView;

/**
 *
 * @author David
 */
public class ImageLoader {
    public static ImageView generateImageView(GameImage gameImage) {
        return new ImageView(gameImage.getImage());
    }
}
