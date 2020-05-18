/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.util;

import javafx.scene.input.KeyCode;

/**
 *
 * @author DavidPrivat
 */
public abstract class KeyEventHandler {
    
    private final KeyCode key;
    public KeyEventHandler(KeyCode keyCode) {
        key = keyCode;
    }
    public KeyCode getKeyCode() {
        return key;
    }
    public abstract void handle();
}
