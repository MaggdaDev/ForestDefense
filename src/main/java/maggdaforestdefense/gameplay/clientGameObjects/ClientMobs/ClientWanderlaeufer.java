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
public class ClientWanderlaeufer extends ClientBug{
    public static double SIZE = 70, STEP_DISTANCE = 20;
    public ClientWanderlaeufer(int id, double x, double y, double hp, Mob.MovementType movement) {
        super(id, x, y, hp, movement, GameObjectType.M_WANDERLAUFER, GameImage.MOB_LAUFKAEFER_1, STEP_DISTANCE, SIZE);
    }
    
    @Override
    public void step() {
        animationState++;
        animationState %= 4;
        switch (animationState) {
            case 0:
            case 2:
                setImage(GameImage.MOB_LAUFKAEFER_1.getImage());
                break;
            case 1:
                setImage(GameImage.MOB_LAUFKAEFER_2.getImage());
                break;
            case 3:
                setImage(GameImage.MOB_LAUFKAEFER_3.getImage());
                break;

        }
    }
}
