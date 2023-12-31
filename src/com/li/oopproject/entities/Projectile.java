package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public abstract class Projectile extends Entity implements Mobile{
    private final int speed;

    // projectiles have 1hp right now but maybe we can make projectiles with more hp so that they can
    // last longer if we want to
    public Projectile(int maxHP, int damage, int speed, Board board){
        super(maxHP, damage, board);
        this.speed = speed;
    }
    public void move(){
        this.setxPos(this.getxPos() + speed);
    }

    public abstract int getLength();
}
