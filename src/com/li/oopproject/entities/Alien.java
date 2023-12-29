package com.li.oopproject.entities;
import com.li.oopproject.Board;

public abstract class Alien extends Entity implements Mobile{

    private final int speed; // how much will an alien move left each update
    public Alien(int hp, int damage, Board board, int speed){ // aliens all move so they have a speed
        super(hp, damage, board);
        this.speed = speed; // since all aliens move they all have a certain speed

    }
    public void Attack(Human human){ // given a human it is in contact with, an alien will attack it
        human.reduceHp(this.getDamage());
    }
    public void move(){
        this.setxPos(this.getxPos()-speed);
    }

    public int getRewardAmount() {
        return 50;
    }
}
