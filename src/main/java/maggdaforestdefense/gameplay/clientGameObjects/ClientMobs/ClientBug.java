/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.gameplay.clientGameObjects.ClientMobs;

import javafx.scene.effect.DropShadow;
import maggdaforestdefense.gameplay.Game;
import maggdaforestdefense.gameplay.clientGameObjects.ClientGameObject;
import maggdaforestdefense.network.NetworkCommand;
import maggdaforestdefense.network.server.serverGameplay.GameObjectType;
import maggdaforestdefense.network.server.serverGameplay.mobs.Bug;
import maggdaforestdefense.network.server.serverGameplay.mobs.Mob;
import maggdaforestdefense.storage.GameImage;
import maggdaforestdefense.storage.Logger;
import maggdaforestdefense.util.GameMaths;

/**
 *
 * @author DavidPrivat
 */
public abstract class ClientBug extends ClientMob {

    private double distance_between_steps = 5;

    protected double distanceSinceLastStep = 0;

    protected int animationState = 0;

    public ClientBug(int id, double x, double y, double hp, Mob.MovementType movementType, GameObjectType objectType, GameImage image, double distanceBetweenSteps, double size) {
        super(id, image, objectType, x, y, hp, movementType, size);
        this.distance_between_steps = distanceBetweenSteps;
    }

    @Override
    public void update(NetworkCommand updateCommand) {
        double newHealth = updateCommand.getNumArgument("hp");
        double newX = updateCommand.getNumArgument("x") - getFitWidth() / 2;
        double newY = updateCommand.getNumArgument("y") - getFitHeight() / 2;
        double dX = newX - xPos;
        double dY = newY - yPos;
        movementType = Mob.MovementType.values()[(int) updateCommand.getNumArgument("movement")];
        distanceSinceLastStep += GameMaths.getAbs(dX, dY);
        if (distanceSinceLastStep >= distance_between_steps) {
            distanceSinceLastStep = 0;
            step();
        }
        if (distanceSinceLastStep != 0) {
            updateRotate(newX, newY);
        }

        setNewPos(newX, newY);
        updateHealth(newHealth);
        updateShadow();

    }

    public void step() {
        animationState++;
        animationState %= 4;
        switch (animationState) {
            case 0:
                setImage(GameImage.MOB_BUG_1.getImage());
                break;
            case 1:
            case 3:
                setImage(GameImage.MOB_BUG_2.getImage());
                break;
            case 2:
                setImage(GameImage.MOB_BUG_3.getImage());
                break;
        }

    }

}
