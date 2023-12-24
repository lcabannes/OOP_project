package com.li.oopproject.entities;
import com.li.oopproject.Board;

public abstract class Alien extends Entity{

    int speed;
    public Alien(int hp, int damage, Board board, int speed){ // aliens all move so they have a speed
        super(hp, damage, board);
        this.speed = speed;

    }
    public void Attack(Human human){ // given a human it is in contact with, an alien will attack it
        human.reduceHp(this.getDamage());
    }
}
