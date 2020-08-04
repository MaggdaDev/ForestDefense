/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.ClientMobs;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;

/**
 *
 * @author David
 */
public class ClientSchwimmkaefer extends ClientBug {
    public ClientSchwimmkaefer(int id, double x, double y, double hp, Mob.MovementType movement) {

        super(id, x, y, hp, movement, GameObjectType.M_SCHWIMMKAEFER, GameImage.MOB_SCHWIMMKAEFER_1, 10);
    }

    @Override
    public void step() {
        animationState++;
        animationState %= 7;
        switch (animationState) {
            case 0:
                setImage(GameImage.MOB_SCHWIMMKAEFER_1.getImage());
                break;
            case 1:
                setImage(GameImage.MOB_SCHWIMMKAEFER_2.getImage());
                break;
            case 2:
                setImage(GameImage.MOB_SCHWIMMKAEFER_3.getImage());
                break;
            case 3:
                setImage(GameImage.MOB_SCHWIMMKAEFER_4.getImage());
                break;
            case 4:
                setImage(GameImage.MOB_SCHWIMMKAEFER_5.getImage());
                break;
            case 5:
                setImage(GameImage.MOB_SCHWIMMKAEFER_6.getImage());
                break;
            case 6:
                setImage(GameImage.MOB_SCHWIMMKAEFER_7.getImage());
                break;
        }

    }
}
