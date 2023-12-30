package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class OctopusAlien extends Alien {
    private final static int maxHP = 30;
    private final static int speed = 1;
    private final static int damage = 10;
    private final static int reloadTime = 500;


    {
        try{
            this.setClassIcon(ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/OctopusAlien.png")));
            this.setClassIcon(GameInterface.resizeImage(this.getClassIcon(), 50, 50));
        }
        catch (IOException e) {
            System.out.println("No image file found for OctopusAlien");
        }
    }
    public OctopusAlien(Board board){
        super(maxHP, damage, board, speed, reloadTime);
    }

}