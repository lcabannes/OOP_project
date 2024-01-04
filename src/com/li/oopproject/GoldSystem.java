package com.li.oopproject;

import com.li.oopproject.entities.Alien;
import com.li.oopproject.entities.AlienShip;
import com.li.oopproject.entities.GhostAlien;
import com.li.oopproject.entities.OctopusAlien;

public class GoldSystem  {
    private int gold;

    public GoldSystem() {
        this.gold = 100;
    }

    public void addGold(int amount) {
        gold += amount;
        if (Game.VERBOSE){
            System.out.println("Current gold: " + gold); // Print the current gold amount
        }
    }

    public int getGold() {
        return gold;
    }

    int getGoldForAlienType(Alien alien) {
        // Example: Different gold amounts for different alien types
        if (gold > 10000){
            return 0;
        }
        if (alien instanceof GhostAlien) {
            return 70;
        } else if (alien instanceof OctopusAlien) {
            return 50;
        } else if (alien instanceof AlienShip) {
            return 100;
        }
        return 5; // Default amount
    }
}
