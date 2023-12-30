package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GhostBuster extends Human{
    public static final int GOLD_COST = 50;
    private final static int reloadTime = 2000;  // reload delay in ms

    private final static int maxHP = 100;
    private final static int damage = 10;

    {
        try{
            this.setClassIcon(ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/GhostBuster.png")));
            this.setClassIcon(GameInterface.resizeImage(this.getClassIcon(), 50, 50));
        }
        catch (IOException e) {
            System.out.println("No image file found for GhostBuster");
        }
    }

    public GhostBuster(Board board){

        super (damage, maxHP, board, reloadTime);
    }


    public Projectile attack(){
        this.setReloadTimeRemaining(reloadTime);
        Projectile laser = new Laser(damage, this.getBoard());
        laser.setxPos(this.getxPos()+50);
        laser.setyPos(this.getyPos());
        return laser;
    }

}

