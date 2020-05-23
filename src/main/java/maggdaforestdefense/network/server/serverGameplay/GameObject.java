/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.gameplay.clientGameObjects.ClientBug;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;

/**
 *
 * @author DavidPrivat
 */
public abstract class GameObject {

    protected final int id;

    public GameObject(int id) {
        this.id = id;
    }

    public abstract CommandArgument[] toNetworkCommandArgs();


    public int getId() {
        return id;
    }
    
    public abstract NetworkCommand update(double timeElapsed);

    public static ClientGameObject generateClientGameObject(NetworkCommand command) {       // ADD HERE FOR NEW MOB
        switch (GameObjectType.values()[(int) command.getNumArgument("type")]) {
            case BUG:
                return new ClientBug((int)command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"));
            default:
                return null;
        }
    }
}
