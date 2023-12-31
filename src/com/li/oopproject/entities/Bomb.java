package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Bomb extends Projectile{
    // the hps of a bullet is how many times it can collide before dying
    private static BufferedImage classIcon;
    private static final int length = 50;
    private static final int height = 25;
    // this initialization block, like all the others, tries to find the image corresponding to the laser
    static {
        try{
            Bomb.classIcon = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Projectiles/Bomb.png"));
            Bomb.classIcon = GameInterface.resizeImage(Bomb.classIcon, length, height);
        }
        catch (IOException e) {
            System.out.println("No image file found for Bomb");
        }
    }

    public Bomb(int damage, Board board){
        super(1, damage, 5, board);
        setInstanceIcon(Bomb.classIcon);
    }



    public int getLength(){
        return length;
    }
}

