/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBug;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles.ClientSpruceShot;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientSpruce;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;

/**
 *
 * @author DavidPrivat
 */
public abstract class GameObject {

    protected final int id;
    protected final GameObjectType gameObjectType;

    public GameObject(int id, GameObjectType t) {
        this.id = id;
        gameObjectType = t;
    }

    public abstract CommandArgument[] toNetworkCommandArgs();


    public int getId() {
        return id;
    }
    
    public abstract NetworkCommand update(double timeElapsed);
    
    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }

    public static ClientGameObject generateClientGameObject(NetworkCommand command) {       // ADD HERE FOR NEW MOB
        switch (GameObjectType.values()[(int) command.getNumArgument("type")]) {
            // MOBS
            case M_BUG:
                return new ClientBug((int)command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"));
            
                
                
                //TOWERS
            case T_SPRUCE:
                return new ClientSpruce((int)command.getNumArgument("id"), (int)command.getNumArgument("xIndex"), (int)command.getNumArgument("yIndex"));
                
                //PROJECTILES
            case P_SPRUCE_SHOT:
                
                return new ClientSpruceShot((int)command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"));
            default:
                throw new UnsupportedOperationException();
        }
    }
}
