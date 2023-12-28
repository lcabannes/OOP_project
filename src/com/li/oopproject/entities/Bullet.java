package com.li.oopproject.entities;

import com.li.oopproject.Board;

public class Bullet extends Projectile{
    // the hps of a bullet is how many times it can collide before dying
    public Bullet(int damage, Board board){
        super(1, damage, 5, board);
    }
}
