package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DefaultHuman extends Human {

    public static final int GOLD_COST = 50;
    private final static int reloadTime = 2000;  // reload delay in ms

    private final static int maxHP = 100;
    private final static int damage = 10;

    private static BufferedImage classIcon;

    static {

        try{
            classIcon = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Humans/DefaultHuman.jpg"));
            classIcon = GameInterface.resizeImage(DefaultHuman.classIcon, getLength(), getHeight());
        }
        catch (IOException e) {
            System.out.println("No image file found for DefaultHuman");
        }

    }

    public DefaultHuman(Board board){
        super (maxHP, damage, board, reloadTime);
        setInstanceIcon(DefaultHuman.classIcon);
    }


    public Projectile attack(){
        this.setReloadTimeRemaining(reloadTime);
        Projectile laser = new Laser(damage, this.getBoard());
        laser.setxPos(this.getxPos()+50);
        laser.setyPos(this.getyPos());
        return laser;
    }

}
