package com.li.oopproject.entities;

import com.li.oopproject.Board;

public class Bullet extends Projectile{
    public Bullet(int damage, Board board){
        super(damage, 20, board);
    }
}
