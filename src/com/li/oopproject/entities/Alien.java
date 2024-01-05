package com.li.oopproject.entities;
import com.li.oopproject.Board;

public abstract class Alien extends Entity implements Mobile, Attacks{
    private int reloadTimeRemaining;
    private int reloadTime;
    private float normalSpeed;      // The normal speed of the alien
    private float currentSpeed;     // The current speed, which may be reduced
    private long speedReductionEndTime; // Time when speed reduction ends

    public Alien(int hp, int damage, Board board, int normalSpeed, int reloadTime){
        super(hp, damage, board);
        this.reloadTimeRemaining = reloadTime;
        this.reloadTime = reloadTime;
        this.normalSpeed = 1;
        this.currentSpeed = normalSpeed;
        this.speedReductionEndTime = 0;
    }
    protected void setReloadTimeRemaining(int reloadTime) {
        reloadTimeRemaining = reloadTime;
    }
    public void reload(int timeElapsed){
        if (reloadTimeRemaining > 0){
            reloadTimeRemaining -= timeElapsed;
        }
    }
    public void Attack(Human human){ // given a human it is in contact with, an alien will attack it
        if (reloadTimeRemaining <= 0) {
            human.reduceHp(this.getDamage());
            reloadTimeRemaining = reloadTime;
        }
    }
    public void move(){
        this.setxPos(this.getxPos()-this.getCurrentSpeed());
    }

    // Method to apply speed reduction
    public void reduceSpeed(float reductionAmount, long duration) {
        if (reductionAmount >= normalSpeed) {
            this.currentSpeed = 0; // Stop the alien completely
        } else {
            this.currentSpeed = normalSpeed - reductionAmount; // Reduce speed
        }
        this.speedReductionEndTime = System.currentTimeMillis() + duration;
    }

    // Method to update the speed (to be called periodically)
    public void updateSpeed() {
        if (System.currentTimeMillis() > speedReductionEndTime) {
            this.currentSpeed = normalSpeed;
        }
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

}
