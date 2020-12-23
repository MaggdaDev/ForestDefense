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
        }}),
    MAPLE_SET(new Upgrade[][]{
        new Upgrade[]{
            Upgrade.MAPLE_1_1,
            Upgrade.MAPLE_1_2,
            Upgrade.MAPLE_1_3,
        }, new Upgrade[]{
            Upgrade.MAPLE_2_1,
            Upgrade.MAPLE_2_2,
            Upgrade.MAPLE_2_3,
        }, new Upgrade[]{
            Upgrade.MAPLE_3_1,
            Upgrade.MAPLE_3_2,
            Upgrade.MAPLE_3_3,}
    }),
    LORBEER_SET(new Upgrade[][]{
        new Upgrade[] {
            Upgrade.LORBEER_1_1,
            Upgrade.LORBEER_1_2,
            Upgrade.LORBEER_1_3,
            Upgrade.LORBEER_1_4
        },
        new Upgrade[]{
            Upgrade.LORBEER_2_1,
            Upgrade.LORBEER_2_2,
            Upgrade.LORBEER_2_3,
            Upgrade.LORBEER_2_4
        },
        new Upgrade[] {
            Upgrade.LORBEER_3_1,
            Upgrade.LORBEER_3_2,
            Upgrade.LORBEER_3_3,
            Upgrade.LORBEER_3_4
        }
    });

    private Upgrade[][] upgrades;

    UpgradeSet(Upgrade[][] array) {
        upgrades = array;
    }
    
    public int getTier(Upgrade upgrade) {
        for(int i = 0; i < upgrades.length; i++) {
            Upgrade[] currUpgrades = upgrades[i];
            for(int j = 0; j < currUpgrades.length; j++) {
                Upgrade currUpgrade = currUpgrades[j];
                if(currUpgrade == upgrade) {
                    return i+1;
                }
            }
        }
        return -1;
    }
    
    public int getType(Upgrade upgrade) {
        for(int i = 0; i < upgrades.length; i++) {
            Upgrade[] currUpgrades = upgrades[i];
            for(int j = 0; j < currUpgrades.length; j++) {
                Upgrade currUpgrade = currUpgrades[j];
                if(currUpgrade == upgrade) {
                    return j+1;
                }
            }
        }
        return -1;
    }

    public Upgrade getUpgrade(int tier, int type) {
        return upgrades[tier - 1][type - 1];
    }

    public Upgrade[][] getUpgrades() {
        return upgrades;
    }

    public Upgrade[] getAllFromTier(int tier) {
        return upgrades[tier - 1];
    }

    public int getMaxTier() {
        return upgrades.length;
    }
}
