package com.li.oopproject.entities;

import com.li.oopproject.Board;
import com.li.oopproject.GameInterface;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class DefaultAlien extends Alien{
    private final static int maxHP = 100;
    private final static int speed = 1;
    private final static int damage = 10;


    {
        try{
            classIcon = ImageIO.read(new File(GameInterface.class.getProtectionDomain().
                    getCodeSource().getLocation().getPath() + "com/li/oopproject/assets/Aliens/DefaultAlien.png"));
            classIcon = GameInterface.resizeImage(classIcon, 50, 50);
        }
        catch (IOException e) {
            System.out.println("No image file found for DefaultAlien");
        }
    }

    public DefaultAlien(Board board){
        super(maxHP, damage, board, speed);
    }
}
