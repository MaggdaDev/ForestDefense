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
            Upgrade.SPRUCE_1_2,
            Upgrade.SPRUCE_1_3,
            Upgrade.SPRUCE_1_4,
            Upgrade.SPRUCE_1_5,
            Upgrade.SPRUCE_1_6
        }, new Upgrade[]{
            Upgrade.SPRUCE_2_1,
            Upgrade.SPRUCE_2_2,
            Upgrade.SPRUCE_2_3,
            Upgrade.SPRUCE_2_4,
            Upgrade.SPRUCE_2_5,
            Upgrade.SPRUCE_2_6
        }, new Upgrade[]{
            Upgrade.SPRUCE_3_1,
            Upgrade.SPRUCE_3_2,
            Upgrade.SPRUCE_3_3,
            Upgrade.SPRUCE_3_4,
            Upgrade.SPRUCE_3_5,
            Upgrade.SPRUCE_3_6
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
    
    public Upgrade[] getAllFromTier(int tier) {
        return upgrades[tier-1];
    }
    
    public int getMaxTier() {
        return upgrades.length;
    }
}
