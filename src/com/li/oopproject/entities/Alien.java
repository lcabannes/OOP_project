package com.li.oopproject.entities;
import com.li.oopproject.Board;

public abstract class Alien extends Entity implements Mobile, Attacks{
    private int reloadTimeRemaining;
    private int reloadTime;
    private final int speed; // how much will an alien move left each update
    public Alien(int hp, int damage, Board board, int speed, int reloadTime){ // aliens all move so they have a speed
        super(hp, damage, board);
        this.speed = speed; // since all aliens move they all have a certain speed
        this.reloadTimeRemaining = reloadTime;
        this.reloadTime = reloadTime;

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
        this.setxPos(this.getxPos()-speed);
    }

    public int getRewardAmount() {
        return 50;
    }
}
