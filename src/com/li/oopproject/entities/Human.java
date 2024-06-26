package com.li.oopproject.entities;
import com.li.oopproject.Board;
public abstract class Human extends Entity{
    private int goldCost = 0;
    static private final int height = 80;
    static private final int length = 80;
    private boolean upgraded = false;

    private int reloadTimeRemaining; // how much ms left until a unit can attack
    public Human(int maxHp, int damage, Board board, int reloadTime){
        super (maxHp, damage, board);
        reloadTimeRemaining = reloadTime;
    }
    public int getReloadTimeRemaining() {
        return reloadTimeRemaining;
    }

    // tell the method how much time has passed so that it now how much it should reload
    public void reload(int timeElapsed){
        if (reloadTimeRemaining > 0) {
            reloadTimeRemaining -= timeElapsed;
        }
    }

    public void upgrade(){
        upgraded = true;
    }

    public abstract Projectile attack();

    protected void setReloadTimeRemaining(int reloadTime) {
        reloadTimeRemaining = reloadTime;
    }

    public static int getLength(){ return length;}

    public static int getHeight(){ return height;}
    public boolean isUpgraded(){ return upgraded;}

    public int getGoldCost(){
        return goldCost;
    }

    public void setGoldCost(int goldCost){
        this.goldCost = goldCost;
    }
}
