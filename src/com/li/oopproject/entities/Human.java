package com.li.oopproject.entities;
import com.li.oopproject.Board;
public abstract class Human extends Entity{
    private int reloadTimeRemaining; // how much ms left until a unit can attack
    public Human(int damage, int hp, Board board, int reloadTime){
        super (damage, hp, board);
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

    public abstract Projectile attack();

    protected void setReloadTimeRemaining(int reloadTime) {
        reloadTimeRemaining = reloadTime;
    }
}
