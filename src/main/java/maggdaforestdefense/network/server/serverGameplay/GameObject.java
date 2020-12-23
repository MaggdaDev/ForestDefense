/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBug;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBlattlaus;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBorkenkaefer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientHirschkaefer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientSchwimmkaefer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientWanderlaeufer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientWasserlaeufer;
import maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles.ClientMapleShot;
import maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles.ClientSpruceShot;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientLorbeer;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientMaple;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientSpruce;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.mobs.Blattlaus;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author DavidPrivat
 */
public abstract class GameObject {

  

    protected final int id;
    protected final GameObjectType gameObjectType;
    private static GameObjectType[] mobs = new GameObjectType[]{GameObjectType.M_BLATTLAUS, GameObjectType.M_HIRSCHKAEFER, GameObjectType.M_WANDERLAUFER};

    public GameObject(int id, GameObjectType t) {
        this.id = id;
        gameObjectType = t;
        
        // SetUpMobs
        /*
        if(mobs == null) {
        ObservableList<GameObjectType> retlist = FXCollections.observableArrayList();
        for(GameObjectType type:GameObjectType.values()) {
            if(type.name().startsWith("M_")) {
                retlist.add(type);
            }
        }
        GameObjectType[] arr = new GameObjectType[retlist.size()];
        for(int i = 0; i < retlist.size(); i++) {
            arr[i] = retlist.get(i);
        }
        mobs = arr;
        }
        */
 
    }

    public abstract CommandArgument[] toNetworkCommandArgs();

    public int getId() {
        return id;
    }

    public abstract NetworkCommand update(double timeElapsed);

    public GameObjectType getGameObjectType() {
        return gameObjectType;
    }
    
    public static GameImage getGameImageFromType (GameObjectType type) {
        switch(type) {
            case M_BLATTLAUS:
                return GameImage.MOB_BLATTLAUS_1;
            case M_HIRSCHKAEFER:
                return GameImage.MOB_HIRSCHKAEFER_1;
            case M_WANDERLAUFER:
                return GameImage.MOB_LAUFKAEFER_1;
            default:
                throw new UnsupportedOperationException();
        }
    }
    
    public static GameObjectType[] getMobs() {
        return mobs;
    }

    public static ClientGameObject generateClientGameObject(NetworkCommand command) {       // ADD HERE FOR NEW MOB
        switch (GameObjectType.values()[(int) command.getNumArgument("type")]) {
            // MOBS
            case M_BORKENKAEFER:
                return new ClientBorkenkaefer((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"), Mob.MovementType.values()[(int) command.getNumArgument("movement")]);
            case M_HIRSCHKAEFER:
                return new ClientHirschkaefer((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"), Mob.MovementType.values()[(int) command.getNumArgument("movement")]);
            case M_SCHWIMMKAEFER:
                return new ClientSchwimmkaefer((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"), Mob.MovementType.values()[(int) command.getNumArgument("movement")]);
            case M_WANDERLAUFER:
                return new ClientWanderlaeufer((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"), Mob.MovementType.values()[(int) command.getNumArgument("movement")]);
            case M_WASSERLAEUFER:
                return new ClientWasserlaeufer((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"), Mob.MovementType.values()[(int) command.getNumArgument("movement")]);
                case M_BLATTLAUS:
                return new ClientBlattlaus((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"), command.getNumArgument("hp"), Mob.MovementType.values()[(int) command.getNumArgument("movement")]);
            //TOWERS
            case T_SPRUCE:
                return new ClientSpruce((int) command.getNumArgument("id"), (int) command.getNumArgument("xIndex"), (int) command.getNumArgument("yIndex"), command.getNumArgument("growingTime"));
                
            case T_MAPLE:
                return new ClientMaple(((int)command.getNumArgument("id")), (int) command.getNumArgument("xIndex"), (int) command.getNumArgument("yIndex"), command.getNumArgument("growingTime"));
                
            case T_LORBEER:
                return new ClientLorbeer((int)command.getNumArgument("id"), (int) command.getNumArgument("xIndex"), (int) command.getNumArgument("yIndex"), command.getNumArgument("growingTime"));

            //PROJECTILES
            case P_MAPLE_SHOT:
                return new ClientMapleShot((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"));
            case P_SPRUCE_SHOT:

                return new ClientSpruceShot((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"));
            default:
                throw new UnsupportedOperationException();
        }
    }
}
