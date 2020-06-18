/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maggdaforestdefense.network.server.serverGameplay;

/**
 *
 * @author DavidPrivat
 */
public enum UpgradeSet {
    SPRUCE_SET(new Upgrade[][]{
        new Upgrade[]{
            Upgrade.SPRUCE_1_1,
            Upgrade.SPRUCE_1_6
        }});

    private Upgrade[][] upgrades;

    UpgradeSet(Upgrade[][] array) {
        upgrades = array;
    }

    public Upgrade getUpgrade(int tier, int type) {
        return upgrades[tier - 1][type - 1];
    }

    public Upgrade[][] getUpgrades() {
        return upgrades;
    }
}
