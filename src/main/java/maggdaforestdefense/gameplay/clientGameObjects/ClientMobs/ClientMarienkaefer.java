/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.ClientMobs;

import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;

/**
 *
 * @author David
 */
public class ClientMarienkaefer extends ClientBug{
    public static double SIZE = 60, STEP_DISTANCE = 5, DELAY_AFTER_TAKEOFF = 30, FLY_DISTANCE = 5;
    private boolean takenOff = false;
    public ClientMarienkaefer(int id, double x, double y, double hp, Mob.MovementType movement) {
        super(id, x, y, hp, movement, GameObjectType.M_MARIENKAEFER, GameImage.MOB_MARIENKAEFER_1, STEP_DISTANCE, SIZE);
    }
    
    @Override
    public void step() {
        animationState++;
        switch(super.movementType){
            
            case WALK:
                if(takenOff) {
                    animationState = 4;
                    takenOff = false;
                    super.distance_between_steps = DELAY_AFTER_TAKEOFF;
                } else {
                    animationState %= 4;
                    super.distance_between_steps = STEP_DISTANCE;
                }
                
                break;
            case FLY:
                if(!takenOff) {
                    animationState = 4;
                    takenOff = true;
                    super.distance_between_steps = DELAY_AFTER_TAKEOFF;
                } else {
                    animationState = 5 + ((1+animationState) % 2);
                    super.distance_between_steps = FLY_DISTANCE;
                }
                break;
        }
        
        switch (animationState) {
            case 0:
            case 2:
                setImage(GameImage.MOB_MARIENKAEFER_1.getImage());
                break;
            case 1:
                setImage(GameImage.MOB_MARIENKAEFER_2.getImage());
                break;
            case 3:
                setImage(GameImage.MOB_MARIENKAEFER_3.getImage());
                break;
            case 4:
                setImage(GameImage.MOB_MARIENKAEFER_4.getImage());
                break;
            case 5:
                setImage(GameImage.MOB_MARIENKAEFER_5.getImage());
                break;
            case 6:
                setImage(GameImage.MOB_MARIENKAEFER_6.getImage());
                break;

        }
    } 
    
}
