/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBug;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBlattlaus;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientBorkenkaefer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientHirschkaefer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientSchwimmkaefer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientWanderlaeufer;
import maggdaforestdefense.gameplay.clientGameObjects.ClientMobs.ClientWasserlaeufer;
import maggdaforestdefense.gameplay.clientGameObjects.clientProjectiles.ClientSpruceShot;
import maggdaforestdefense.gameplay.clientGameObjects.clientTowers.ClientSpruce;
import maggdaforestdefense.network.CommandArgument;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;

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

            //PROJECTILES
            case P_SPRUCE_SHOT:

                return new ClientSpruceShot((int) command.getNumArgument("id"), command.getNumArgument("x"), command.getNumArgument("y"));
            default:
                throw new UnsupportedOperationException();
        }
    }
}
