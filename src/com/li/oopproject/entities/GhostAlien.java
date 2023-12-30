package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GhostAlien extends Alien {

    private final static int maxHP = 30;
    private final static int speed = 1;
    private final static int damage = 40;

    private final static int reloadTime = 2000;

    {
        try{
            this.setClassIcon(ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/GhostAlien.png")));
            this.setClassIcon(GameInterface.resizeImage(this.getClassIcon(), getLength(), getHeight()));
        }
        catch (IOException e) {
            System.out.println("No image file found for GhostAlien");
        }
    }
    public GhostAlien(Board board){
        super(maxHP, damage, board, speed, reloadTime);
    }
}