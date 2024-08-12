/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package language;

/**
 *
 * @author ist actually gar nicht DavidPrivat sondern alex; enalex
 */
public class English extends Language{  // Halle (Saale) do you check it

    public English() {
    // Tower names
    SPRUCE_NAME = "Spruce";
    MAPLE_NAME = "Maple";
    LORBEER_NAME = "Laurel";
    OAK_NAME = "Oak";
    
    // Tower descriptions
    SPRUCE_DESCRIPTION = "Spruce trees shoot straight needle shots that can penetrate 1 enemy. Truly the most basic tree.";
    MAPLE_DESCRIPTION = "Maples shoot rings of leaves around it, causing area damage.";
    LORBEER_DESCRIPTION = "Laurel trees have a toggled attack which deals very little damage. When this attack kills an enemy, a laurel is grown, which can be harvested for money.";
    OAK_DESCRIPTION = "Oaks do not attack, but instead have a lot of health and are meant for the front lines of base defense. They also slowly regenerate.";
        
        
    // Upgrade description
    // MAGGDA WARUM HIESSEN DIE ALLE "SPURCE" SIE NIGGRE
    UPGRADE_DESCRIPTION_SPRUCE_1_1 = "Needle division: If a needle hits an enemy, it splits into more needles, which do not split further.";
    UPGRADE_DESCRIPTION_SPRUCE_1_2 = "Spruce monoculture: Attack speed of spruce trees now scales with the amount of spruces on the map.";
    UPGRADE_DESCRIPTION_SPRUCE_1_3 = "Armor-pen. needles: Spruce deals more damage to armored enemies.";
    UPGRADE_DESCRIPTION_SPRUCE_1_4 = "Growing taller: Can now hit flying enemies.";
    UPGRADE_DESCRIPTION_SPRUCE_1_5 = "Strengthening of needle: If a needle hits an enemy, it deals more damage to the enemy behind it.";
    UPGRADE_DESCRIPTION_SPRUCE_1_6 = "Life steal: Spruce regenerates a part of the damage it deals.";
    
    UPGRADE_DESCRIPTION_SPRUCE_2_1 = "Desperate Strength: The more enemies are in the range of the spruce, the more needles it shoots.";
    UPGRADE_DESCRIPTION_SPRUCE_2_2 = "Spruce Fury: Over the course of a round, the spruce increases its rate of fire for each hit it lands.";
    UPGRADE_DESCRIPTION_SPRUCE_2_3 = "Critical needles: Some needles strike critically and deal more damage.";
    UPGRADE_DESCRIPTION_SPRUCE_2_4 = "Root strike: Can now attack burrowing enemies with its roots.";
    UPGRADE_DESCRIPTION_SPRUCE_2_5 = "Merciless Spruce: If a needle kills an enemy, it can penetrate an extra enemy.";
    UPGRADE_DESCRIPTION_SPRUCE_2_6 = "Spruce Friendship: Increases the life regeneration of nearby spruces.";

    UPGRADE_DESCRIPTION_SPRUCE_3_1 = "Serial murderer (undertale reference): Shoots an extra needle per killed enemy.";
    UPGRADE_DESCRIPTION_SPRUCE_3_2 = "Spruce Frenzy: Kills on enemies increase rate of fire permanently.";
    UPGRADE_DESCRIPTION_SPRUCE_3_3 = "Giant scare (bruh): The spruce deals additional damage scaling with maximum health of enemies hit.";
    UPGRADE_DESCRIPTION_SPRUCE_3_4 = "Spruce Research: The spruce recognises weakpoints in enemies and permanently increases damage to that type of enemy.";
    UPGRADE_DESCRIPTION_SPRUCE_3_5 = "Dominating Needles: Needles do more damage the longer they are airborne.";
    UPGRADE_DESCRIPTION_SPRUCE_3_6 = "If the spruce's HP fall under 30%, other spruce trees on the map sacrifice their HP to heal it.";
    
    
    UPGRADE_DESCRIPTION_MAPLE_1_1 = "Expansion: The less enemies are in the range of the maple, the more damage it deals.";
    UPGRADE_DESCRIPTION_MAPLE_1_2 = "Maple Alliance: Increases the range of other maples nearby.";
    UPGRADE_DESCRIPTION_MAPLE_1_3 = "Death be upon the Greedy: The maple deals more damage proportional to the speed of enemies.";
    
    UPGRADE_DESCRIPTION_MAPLE_2_1 = "Escalation: If there are many enemies in the maple's immediate vicinity, it activates 'Escalation' and shoots faster for a short time.";
    UPGRADE_DESCRIPTION_MAPLE_2_2 = "Warm-up: After sitting idle for a decent amount of time, the maple's first attack deals more damage.";
    UPGRADE_DESCRIPTION_MAPLE_2_3 = "Exhausting leaves: Enemies hit by the maple have their defense reduced and take more damage from all sources.";
    
    UPGRADE_DESCRIPTION_MAPLE_3_1 = "Crushing leaves: Instead of dealing damage, the maple permanently destroys enemies' armor points equal to the damage it would have dealt.";
    UPGRADE_DESCRIPTION_MAPLE_3_2 = "Tear them down: Each enemy hit by the maple makes the leaves fly a bit further.";
    UPGRADE_DESCRIPTION_MAPLE_3_3 = "Disassembling leaves: The maple's attack speed increases permanently for each enemy killed.";
    
    //holy fuck, do you hear laurel or yanny??
    UPGRADE_DESCRIPTION_LORBEER_1_1 = "Long-range harvest: The laurel tree can now attack at a greater distance.";
    UPGRADE_DESCRIPTION_LORBEER_1_2 = "Fruitful harvest: Each laurel is worth more money.";
    UPGRADE_DESCRIPTION_LORBEER_1_3 = "Efficient harvest: Time between attacks is reduced.";
    UPGRADE_DESCRIPTION_LORBEER_1_4 = "Stock up: More laurels can be hanging from the tree at the same time";
            
    UPGRADE_DESCRIPTION_LORBEER_2_1 = "Kill confirm: The lower the enemies' HP when hit by the laurel swipe, the more damage will be dealt.";
    UPGRADE_DESCRIPTION_LORBEER_2_2 = "Harvest rush: if many enemies are killed at once, the cooldown until the next attack is greatly reduced.";
    UPGRADE_DESCRIPTION_LORBEER_2_3 = "Bulk discount: If multiple laurels are sold at the same time, each laurel is worth more (scales).";
    UPGRADE_DESCRIPTION_LORBEER_2_4 = "Happiness in Miss Fortune (laial): Enemies hit, but not killed, by the laurel swipe reward a bit more money on death.";
            
    UPGRADE_DESCRIPTION_LORBEER_3_1 = "Auto-harvest: If an enemy can be killed by the swipe, the attack will be triggered automatically.";
    UPGRADE_DESCRIPTION_LORBEER_3_2 = "Prestige Harvest: If the laurel tree carries the maximum amount of laurels, they can instead be turned into a permanent upgrade, making the tree 20% more efficient. (Do not stack this with 1.4)";
    UPGRADE_DESCRIPTION_LORBEER_3_3 = "Headhunter: Completion of certain bounties is rewarded with the maximum amount of laurels.";
    UPGRADE_DESCRIPTION_LORBEER_3_4 = "Trade offer: If the laurel tree carries the maximum amount of laurels, they can instead be traded for a random upgrade on a different, random tree.";
    
    
    UPGRADE_DESCRIPTION_OAK_1_1 = "Tough bark: The Oak tree has more hit points.";
    UPGRADE_DESCRIPTION_OAK_1_2 = "Yummy acorns: Flying enemies will prioritise the oak tree over other trees, to get a taste of its delicious acorns.";
    UPGRADE_DESCRIPTION_OAK_1_3 = "Yummy roots: Burrowed enemies will prioritise the oak tree over other trees, to get a taste of its delicious roots.";
    UPGRADE_DESCRIPTION_OAK_1_4 = "Stingy bark: Enemies attacking the oak tree will take more damage.";
            
    UPGRADE_DESCRIPTION_OAK_2_1 = "Rejuvenate: The Oak tree regenerates its health at the end of each round.";
    UPGRADE_DESCRIPTION_OAK_2_2 = "Spring Water: If the oak tree is next to water, it will regenerate faster.";
    UPGRADE_DESCRIPTION_OAK_2_3 = "Socially acceptable oak: The oak tree donates some of its hit points to other trees in need.";
    UPGRADE_DESCRIPTION_OAK_2_4 = "Connected roots: The oak tree regenerates its health, based on the damage other nearby trees take.";
            
    UPGRADE_DESCRIPTION_OAK_3_1 = "Full heal: (ability) Fully replenishes the oak tree's health.";
    UPGRADE_DESCRIPTION_OAK_3_2 = "Oak Fort: Multiple Oak trees in a row work together to form a fort; they now share all damage taken.";
    UPGRADE_DESCRIPTION_OAK_3_3 = "Spontaneous hardening: (ability) The next attack on the oak tree does not deal damage.";
    UPGRADE_DESCRIPTION_OAK_3_4 = "Oak harvest: For every enemy attacking the oak tree, it gains permanent maximum health.";

}
    
}
