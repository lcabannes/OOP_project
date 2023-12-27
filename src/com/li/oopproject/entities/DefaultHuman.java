package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DefaultHuman extends Human {
    private final static int reloadTime = 1000;  // reload delay in ms

    private final static int maxHP = 100;
    private final static int damage = 10;

    {
        try{
            this.setClassIcon(ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/DefaultHuman.jpg")));
            this.setClassIcon(GameInterface.resizeImage(this.getClassIcon(), 50, 50));
        }
        catch (IOException e) {
            System.out.println("No image file found for DefaultHuman");
        }
    }

    public DefaultHuman(Board board){

        super (damage, maxHP, board, reloadTime);
    }


    public Projectile attack(){
        Projectile bullet = new Bullet(damage, this.getBoard());
        bullet.setxPos(this.getxPos()+50);
        bullet.setyPos(this.getyPos());
        return bullet;
    }

}
